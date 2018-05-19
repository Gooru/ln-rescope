package org.gooru.rescope.infra.services;

import java.util.List;
import java.util.UUID;

import org.gooru.rescope.infra.data.RescopeContext;
import org.gooru.rescope.infra.data.RescopeQueueModel;
import org.gooru.rescope.infra.utils.CollectionUtils;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 18/5/18.
 */
class RescopeRequestQueueServiceImpl implements RescopeRequestQueueService {

    private final DBI dbi;
    private static final Logger LOGGER = LoggerFactory.getLogger(RescopeRequestQueueService.class);
    private RescopeContext context;
    private RescopeRequestQueueDao queueDao;

    RescopeRequestQueueServiceImpl(DBI dbi) {
        this.dbi = dbi;
    }

    @Override
    public void enqueue(RescopeContext context) {
        this.context = context;
        System.out.println("Received request for queueing");
        System.out.println(context.toString());
        queueDao = dbi.onDemand(RescopeRequestQueueDao.class);
        if (context.getClassId() != null) {
            doQueueingForClass();
        } else {
            doQueueingForIL();
        }

    }

    private void doQueueingForIL() {
        if (context.getCourseId() == null) {
            LOGGER.warn("Rescope fired for IL without courseId. Abort.");
            return;
        }
        if (!queueDao.isCourseNotDeleted(context.getCourseId())) {
            LOGGER.warn("Rescope fired for deleted or not existing course: '{}'. Abort.", context.getCourseId());
            return;
        }
        queueInDb();
    }

    private void doQueueingForClass() {
        if (!queueDao.isClassNotDeletedAndNotArchived(context.getClassId())) {
            LOGGER.warn("Rescope fired for archived or deleted class: '{}'", context.getClassId());
            return;
        }
        UUID courseId = queueDao.fetchCourseForClass(context.getClassId());
        if (courseId == null) {
            LOGGER.warn("No course associated with class: '{}'. Will not rescope", context.getClassId());
            return;
        }

        if (context.getCourseId() != null && !context.getCourseId().equals(courseId)) {
            LOGGER.warn("Course specified in request '{}' does not match course associated with class '{}'. Will use "
                + "the one associated with class", context.getCourseId(), courseId);
        }

        populateMemberships(courseId);
        queueInDb();
    }

    private void populateMemberships(UUID courseId) {
        if (context.isOOBRequestForRescope() || context.areUsersJoiningClass()) {
            // Validate membership of provided users
            List<UUID> existingMembersOfClassFromSpecifiedList = queueDao
                .fetchSpecifiedMembersOfClass(context.getClassId(),
                    CollectionUtils.convertFromListUUIDToSqlArrayOfUUID(context.getMemberIds()));

            if (existingMembersOfClassFromSpecifiedList.size() < context.getMemberIds().size()) {
                LOGGER.warn("Not all specified users are members of class. Will process only members");
            }
            context = context.createNewContext(existingMembersOfClassFromSpecifiedList, courseId);
        } else {
            List<UUID> members = queueDao.fetchMembersOfClass(context.getClassId());
            context = context.createNewContext(members, courseId);
        }
    }

    private void queueInDb() {
        queueDao.queueRequests(context.getMemberIds(), RescopeQueueModel.fromRescopeContextNoMembers(context));
    }

}
