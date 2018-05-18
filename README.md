# Rescope of Courses for User (IL and In class flows)

In a course which is catering to many competencies, users may not have to study all the items specified in that course. During study play experience, this is taken care by skip logic coded with navigate-map content. Similar kind of experience is needed on course map to display the content which should be focus for the user. 

### Rescope datastore

Since there is a need to keep skip logic of navigate map same as rescope logic, rescope logic would use the same data store to ascertain the completion/mastery of competency as is used by navigate map. 

### Pilot scope

For pilot, here are salient points of implementation:

- There will be two APIs exposed, one which will fetch the rescope course content and another which can trigger the rescoping of specified content for specified user
- The fetch API will return the rescoped course if, 
    - user is doing IL and session token belongs to same user
    - or if user is in class, then
        - session token should be of either be teacher/co-teacher of class and user for which rescoped course is fetched should be student of same class
        - or the user for which rescoped course is being fetched, should be the same user for which session token is being used
- If the rescoped course for user does not exists, then Http status of 404 will be sent. In addition, a request is going to be queued to create the rescoped version of course for specified user. The downstream services will take care of not doing same job multiple times
- Once the rescope is done for a user, class and course combination, it will be persisted. Next time onwards when rescope APIs are called, they will either fetch persisted data (in case of fetch API) or the downstream queue processor will make sure to check if rescoping is not done so far before doing actual work


Since, there is a need to persist this queue of requests, we should be using DB to maintain the list and update the status there. This in turn will give rise to batch job kind of model, where in Http API will keep on updating the queue, and batch model will pick up and do rescoping of content
 
### Build and Run

To create the shadow (fat) jar:

    ./gradlew

To run the binary which would be fat jar:

    java -jar rescope.jar -Dconfig.file=src/main/resources/rescope.json

### Fetch Rescope API
- For rescope to be enabled, class setting should have rescope as true or in case of IL course version should be not null
- If this API is called for class or course (IL) for which rescope is not enable, it will throw an error
- if rescope is enabled, then look up in rescope store to see if there is rescoped data available
    - If available, serve the data
    - Else, send 404. But also send a message to event bus so that processing request can be queued
- Note that currently there are no checks to verify if class and course specified (in class context) are having an association. TODO

### Do Rescope API
- This API will be called internally only
- It will return 200 response directly and will delegate processing to worker threads on message bus
- The API payload would contain source, class id and member ids array (optional, present only in case where source is class join)
- First check to see if class is having rescope enabled. If not, then processing is done
- Now generate the data for messages based on source
    - class join, one event per member specified in payload will be created.
    - course assign to class, class members will be looked up and one event per member of class will be created
    - rescope setting change, class members will be looked up and one event per member of class will be created
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

