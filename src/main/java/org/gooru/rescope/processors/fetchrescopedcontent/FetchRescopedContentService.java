package org.gooru.rescope.processors.fetchrescopedcontent;

import org.gooru.rescope.infra.constants.HttpConstants;
import org.gooru.rescope.infra.exceptions.HttpResponseWrapperException;
import org.gooru.rescope.infra.services.rescopeapplicable.RescopeApplicableService;
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

    private void validateUserIsReallyTeacher() {
        if (!getDao().isUserTeacherOrCollaboratorForClass(command.asBean())) {
            throw new HttpResponseWrapperException(HttpConstants.HttpStatus.FORBIDDEN,
                "You need to be teacher or co-teacher for this class");
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
        return result;
    }
}
