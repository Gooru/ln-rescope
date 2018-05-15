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

