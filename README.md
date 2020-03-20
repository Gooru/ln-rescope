# Rescope of Courses for User (IL and In class flows)

In a course which is catering to many competencies, users may not have to study all the items specified in that course. During study play experience, this is taken care by skip logic coded with navigate-map content. Similar kind of experience is needed on course map to display the content which should be focus for the user. 

### Rescope Specifications
The specification document resides [here](https://docs.google.com/document/d/1ED2MHbTLtEDym4KM163VbYgLPR_wwtCXe8q8xQNDHpU/edit?ts=5b84f144#heading=h.16lv0v7a06wr)


### Rescope datastore

Rescope will be aware of the progression of competencies in the domain. Hence it will access the store where baseline for users are stored, domain competency matrix is available along with progression info. In addition, it needs to read course/class/collection information. Once done it will need to store the output somewhere.

### Technical Scope


- There will be two APIs exposed, one which will fetch the rescope course content and another which can trigger the rescoping of specified content for specified user
- The fetch API will return the rescoped course if, 
    - user is doing IL and session token belongs to same user
    - or if user is in class, then
        - session token should be of either be teacher/co-teacher of class and user for which rescoped course is fetched should be student of same class
        - or the user for which rescoped course is being fetched, should be the same user for which session token is being used
- If the rescoped course for user does not exists, then Http status of 404 will be sent. Note that there won't be any queue-ing done for the request. The downstream services will take care of not doing same job multiple times
- Once the rescope is done for a user, class and course combination, it will be persisted. Next time onwards when rescope APIs are called, they will directly fetch the persisted data. If the do rescope API is called again, the processors will check to validate if rescoping is not done so far before doing actual work


Since, there is a need to persist this queue of requests, we should be using DB to maintain the list and update the status there. This in turn will give rise to batch job kind of model, where in Http API will keep on updating the queue, and batch model will pick up and do rescoping of content
 
### Build and Run

To create the shadow (fat) jar:

    ./gradlew

To run the binary which would be fat jar from the project base directory:

    java -jar rescope.jar $(project_loc)/src/main/resources/rescope.json

### Fetch Rescope API
- For rescope to be enabled, the course associated with class should be a premium course
- If this API is called for class or course (IL) for which rescope is not enabled, it will throw an error
- if rescope is enabled, then look up in rescope store to see if there is rescoped data available
    - If available, serve the data
    - Else, send 404. But also send a message to event bus so that processing request can be queued
- Note that currently there are no checks to verify if class and course specified (in class context) are having an association

### Do Rescope API
- This API will be called internally only
- It will return 200 response directly and will delegate processing to worker threads on message bus
- The API payload would contain source, class id and member ids array (optional, present only in case where source is class join)
- First check to see if class is having rescope enabled. If not, then processing is done
- Now generate the data for messages based on source
    - class join, one event per member specified in payload will be created.
    - course assign to class, class members will be looked up and one event per member of class will be created
    - OOB, this won't be used by API per se, but by READ handler to post message along with member id in case of 404
        - This may also be used as API to trigger rescope on adhoc basis
- Note that here we won't validate if class member may have (with current UI flows may not be possible) rescoped content already. That will be done downstream

### Design

#### Problem
- Have a batch processing kind of infra
- Should be able to do it in parallel
- Queueing should be backed by persistence
- Queueing should be open so that others can also request for queueing their stuff
- Avoid concurrent processing of same record

#### Solution
- Have a batch processing kind of infra using parallelization would be achieved using worker threads working over message bus
- For persistence DB will be used
- Event bus will provide a mechanism for open ended trigger end point
- Do redundant checks and store state to avoid multiple processing of same record

#### Facets of Design
- Core processing
    - Should be triggered with receiving some key to identify as to what needs processing
- Controller processing
    - Event bus to receive two kinds of messages
        - Queue
        - Process
    - Both of these events will be fire and forget
    - For Queue messages, entry will be made into DB table
    - Now some mechanism needs to be in place to to read the DB table and generate Process messages
    - Need a timer thread in place
    - The queue in DB is going to have a status field with values - null, dispatched, processing
    - Record will be inserted in queue with status as null
    - When timer thread picks that up and sends to message bus, it will be marked as dispatched
    - When worker threads pick up the record to process, they first check to see if the record is present in table with status as dispatched and record is not present in rescope table, then the record will be marked as processing and will be processed
    - Once processing is done, this record will be deleted from queue
    - For the first run of timer thread, it should clean up all statuses in DB queue so that they are picked up for processing downstream
    - The number of records that are read from DB/queue and dumped on to message bus for processing, needs to be configurable

### Task list V2 (Complete infrastructure)
- Modify Rescope with new Algorithm (progression aware, API changes, no queueing on fetch etc)
- Modify Route0 with new Algorithm (read from baseline table, API changes, remove queueing of request on fetch etc)
- Modify baseline LP to have Union of LP and class Floor line
- Modify baseline LP to trigger Route0/Rescope if applicable
- Modify baseline LP to cater to new table structures (may have impact on read API for baseline LP)
- Modify Navigate Next to consider the rescoped content
- Modify class API to expose relevant settings

### New Rescope flow
- Validate if rescope is already done
    - if done, and if there is a flag which says to override, then delete the current rescope
    - if done, and there is no flag, DONE
    - if not done, continue
- Verify if rescope is applicable. If yes, continue else DONE
- Validate class/course/user (class exists, course exists and is assigned to that class, and user belongs to that class)
- Verify if the course has subject bucket set up. If yes, continue else DONE
- Verify if the baseline LP for the specified class/course/user exists. If yes, continue else DONE
- Find floor and ceil for the class
- Find the Baseline LP for user for that class
- Find the course's competency route
- BoundedContext = baselineProfile, classCeiling
- create Competency Map for subject
- CompetencyMap = CompetencyMap.trimAboveCompetencyLine(classCeiling)
- CompetencyMap = CompetencyMap.trimBelowCompetencyLine(baselineProfile)
- fetch all collections/assessments from db with their course, unit, lesson ids, gut_codes, order by course, unit, lesson, collection sequence which are non deleted
- Lookup domain competency matrix for that subject and create a lookup of gut code to domain
- iterate over units
    - iterate over lessons
        - iterate over collections
            - for a collection, get the gut codes
                - Look up domain for that gut code
                - if any gut code/domain is present in the adjustedStudyRouteForUser, keep it
                - if all are absent, skip it
        - if there are any collections, in this lesson, stash collections in lesson
    - if there are any lesson in this unit, keep the unit. Else skip unit as well
- once done store the output

## Technical drilldown: Package structure and functions

Following is the list of packages and its contents. Note that abbreviated package names are used.

### o.g.r.bootstrap 
Contains the main runner class which has the main method.

### o.g.r.bootstrap.verticles
Housing for the verticles. There are four verticles as of now

### o.g.r.bootstrap.verticles.AuthVerticle
Authenticates session token with Redis

### o.g.r.bootstrap.verticles.HttpVerticle
Responsible for starting up HTTP server and registering routes

### o.g.r.bootstrap.verticles.RescopeVerticle
The verticle which is main listener for API requests which is forwarded from Http server post authentication. 

### o.g.r.bootstrap.verticles.RescopeProcessingVerticle
This verticles receives a message for the queue record of rescope for processing. It takes that record and does the processing. Other components (even outside the process scope) can queue the records to get it processed.

### o.g.r.infra.components
This contains various components like config handler, data source registry etc. This also has mechanism to initialize components at the startup. Components are generally singleton.

### o.g.r.infra.components.RescopeQueueReaderAndDispatcher
This is the timer based runner class which is responsible to read the Persisted queued requests and send them to Event bus so that they can be processed by listeners. It does wait for reply, so that we do increase the backpressure on TCP bus too much, however what is replied is does not matter as we do schedule another one shot timer to do the similar stuff. For the first run, it re-initializes the status in the DB so that any tasks that were under processing when the application shut down happened would be picked up again.

### o.g.r.infra.constants
Housing for different constants used across the application.

### o.g.r.infra.data
This contains general POJO which are reusable across different modules in this application. 

### o.g.r.infra.exceptions
This contains exception classes which are reusable across different modules in this application. 

### o.g.r.infra.jdbi
This is JDBI specific package which contains helper entities like reusable mappers, argument factories, creators etc. This does not contain module specific DAO though. They are hosted with individual modules.

### o.g.r.infra.services
This houses infra structure services. These are different from domain services. 

### o.g.r.infra.services.core
Entry point for rescope processing. Here processing is just about calculating and not about persisting

### o.g.r.infra.services.core.algebra.competency 
This package houses the whole algebra aspects of competency. This includes, but not limited to:
- Competency model
- Domain model
- Competency line 
- Competency Path
- Competency Route
- Progression Level (sequence id of competency)
- Subject model

This is base package responsible for doing algebra and unless there is a need to change the way algebra functions, this should be pretty constant.

### o.g.r.infra.services.core.algebra.competency.mappers
Houses JDBI mappers for entities contributing to competency algebra

### o.g.r.infra.services.core.competencylinefinder
Module to fetch different competency lines (ceiling line/floor line) based on context (either from class high/low setting or course)

### o.g.r.infra.services.core.competencymapcreator
Module to create competency map which is entry point to competency algebra

### o.g.r.infra.services.core.competencypresencechecker
Module to check if the competency is present in specified domain competency map. 

### o.g.r.infra.services.core.coursecompetencyfetcher
Module to fetch competencies from specified course. The competencies are read from aggregated_gut_codes column in db.

### o.g.r.infra.services.core.subjectinferer
Module used to infer subject either from class/course based on context

### o.g.r.infra.services.core.validators
Module to validate different aspects of context for class/user

### o.g.r.infra.services.itemfilter
This is the entry point for the flow to obtain the skipped items in given context. The context could be course, unit or lesson. Right now we only support context of course. When other contexts are needed, we need to provide extensions in form of alternate implementations of this interface.

### o.g.r.infra.services.queueoperators
This module houses operations on queue record like
- checking eligibility of processing based on class settings
- initializing the queue once the application starts
- mechanism to dispatch the records to processing verticle
- mechanism to queue and/or dequeue the request

### o.g.r.infra.services.rescopeapplicable
 The module to determine whether the diagnostic is applicable or not in specified context.
 
### o.g.r.infra.services.rescoperequest
This module is responsible for queueing the request

### o.g.r.infra.utils
Different utility classes

### o.g.r.processors
The processors which are used as handlers for APIs

### o.g.r.processors.dorescopeofcontent
The processing handlers for API backend to catch request for doing rescope of content. This results in queue of request.

### o.g.r.processors.fetchrescopedcontent
The processing handlers for API backend to fetch rescoped content for a specified user/class context.



