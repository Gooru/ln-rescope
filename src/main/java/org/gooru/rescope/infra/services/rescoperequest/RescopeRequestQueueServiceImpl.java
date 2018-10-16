package org.gooru.rescope.infra.services.rescoperequest;

import java.util.UUID;
import org.gooru.rescope.infra.data.RescopeContext;
import org.gooru.rescope.infra.data.RescopeQueueModel;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 18/5/18.
 */
class RescopeRequestQueueServiceImpl implements RescopeRequestQueueService {

  private static final Logger LOGGER = LoggerFactory.getLogger(RescopeRequestQueueService.class);
  private final DBI dbi;
  private RescopeContext context;
  private RescopeRequestQueueDao queueDao;

  RescopeRequestQueueServiceImpl(DBI dbi) {
    this.dbi = dbi;
  }

  @Override
  public void enqueue(RescopeContext context) {
    this.context = context;
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
      LOGGER.warn("Rescope fired for deleted or not existing course: '{}'. Abort.",
          context.getCourseId());
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
      LOGGER.warn(
          "Course specified in request '{}' does not match course associated with class '{}'. Will use "
              + "the one associated with class", context.getCourseId(), courseId);
    }

    if (!queueDao.isValidMemberOfClass(context.getClassId(), context.getUserId())) {
      LOGGER.warn("User '{}' is not valid member of class '{}'", context.getUserId(),
          context.getClassId());
    }
    queueInDb();
  }

  private void queueInDb() {
    queueDao.queueRequest(RescopeQueueModel.fromRescopeContext(context));
  }

}
