package org.gooru.rescope.processors.fetchrescopedcontent;

import org.gooru.rescope.infra.constants.HttpConstants;
import org.gooru.rescope.infra.exceptions.HttpResponseWrapperException;
import org.gooru.rescope.infra.services.RescopeApplicableService;
import org.gooru.rescope.infra.services.RescopeRequestQueueService;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 17/5/18.
 */
class FetchRescopedContentService {

    private final DBI dbi;
    private FetchRescopedContentCommand command;

    FetchRescopedContentService(DBI dbi) {
        this.dbi = dbi;
    }

    String fetchRescopedContent(FetchRescopedContentCommand command) {
        this.command = command;
        String result;

        if (command.getClassId() != null) {
            if (command.isTeacherContext()) {
                validateUserIsReallyTeacher();
            }
            result = fetchRescopedContentForClass();
        } else {
            result = fetchRescopedContentForIL();
        }
        queueRescopeContentRequestIfNeeded(result);
        return result;
    }

    private void validateUserIsReallyTeacher() {
        if (!getDao().isUserTeacherOrCollaboratorForClass(command.asBean())) {
            throw new HttpResponseWrapperException(HttpConstants.HttpStatus.FORBIDDEN,
                    "You need to be teacher or co-teacher for this class");
        }
    }

    private void queueRescopeContentRequestIfNeeded(String result) {
        if (result == null) {
            RescopeRequestQueueService service = RescopeRequestQueueService.build();
            service.enqueue(command.asRescopeContext());
        }
    }

    private String fetchRescopedContentForIL() {
        if (RescopeApplicableService.isRescopeApplicableToCourseInIL(command.getCourseId())) {
            return getDao().fetchRescopedContentForUserInIL(command.asBean());
        } else {
            throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
                    "Rescope not applicable to specified course/class");
        }

    }

    private String fetchRescopedContentForClass() {
        if (RescopeApplicableService.isRescopeApplicableToClass(command.getClassId())) {
            return getDao().fetchRescopedContentForUserInClass(command.asBean());
        } else {
            throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
                    "Rescope not applicable to specified course/class");
        }
    }

    private FetchRescopedContentDao getDao() {
        return dbi.onDemand(FetchRescopedContentDao.class);
    }
}
