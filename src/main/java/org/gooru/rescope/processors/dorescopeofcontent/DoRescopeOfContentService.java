package org.gooru.rescope.processors.dorescopeofcontent;

import org.gooru.rescope.infra.services.rescopeapplicable.RescopeApplicableService;
import org.gooru.rescope.infra.services.rescoperequest.RescopeRequestQueueService;
import org.gooru.rescope.infra.utils.CollectionUtils;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for queueing of requests for rescope.
 * <p>
 * Note that this class only handles the override flag, and if override is present it removes the
 * rescope content records for the specified users or for whole class based on type of rescope
 * source. Then it delegates to downstream for processsing. In case override is not present, it just
 * delegates to downstream. Downstream processing services check for rescope already done before
 * they actually process the rescope request from the queue.
 *
 * @author ashish on 18/5/18.
 */
class DoRescopeOfContentService {

  private final DBI dbi;
  private DoRescopeOfContentCommand command;
  private DoRescopeOfContentDao dao = null;
  private static final Logger LOGGER = LoggerFactory.getLogger(DoRescopeOfContentService.class);

  DoRescopeOfContentService(DBI dbi) {
    this.dbi = dbi;
  }

  void doRescope(DoRescopeOfContentCommand command) {
    this.command = command;
    if (command.getClassId() != null) {
      doRescopeInClass();
    } else {
      doRescopeForIL();
    }
  }

  private void doRescopeForIL() {
    if (RescopeApplicableService.isRescopeApplicableToCourseInIL(command.getCourseId())) {
      if (command.isOverride()) {
        resetRescopeForSpecifiedMembersForIL();
      }
      queueRescope();
    }
  }

  private void doRescopeInClass() {
    if (RescopeApplicableService.isRescopeApplicableToClass(command.getClassId())) {
      if (command.isOverride()) {
        if (command.applyToAllMembers()) {
          resetRescopeForWholeClass();
        } else if (command.hasMembershipInfo()) {
          resetRescopeForSpecifiedMembersInClass();
        }
      }
      queueRescope();
    }
  }

  private void resetRescopeForSpecifiedMembersForIL() {
    fetchDao().resetRescopeInfoForILForSpecifiedUsers(
        CollectionUtils.convertFromListUUIDToSqlArrayOfUUID(command.getMemberIds()),
        command.getCourseId());
  }

  private void resetRescopeForSpecifiedMembersInClass() {
    fetchDao().resetRescopeInfoInClassForSpecifiedUsers(
        CollectionUtils.convertFromListUUIDToSqlArrayOfUUID(command.getMemberIds()),
        command.getCourseId(),
        command.getClassId());
  }

  private void resetRescopeForWholeClass() {
    fetchDao().resetRescopeInfoInClassForAllUsers(command.getCourseId(), command.getClassId());
  }


  private void queueRescope() {
    RescopeRequestQueueService service = RescopeRequestQueueService.build();
    service.enqueue(command.asRescopeContext());
  }

  private DoRescopeOfContentDao fetchDao() {
    if (dao == null) {
      dao = dbi.onDemand(DoRescopeOfContentDao.class);
    }
    return dao;
  }
}
