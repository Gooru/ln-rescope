package org.gooru.rescope.processors.fetchrescopedcontent;

import org.gooru.rescope.infra.constants.HttpConstants;
import org.gooru.rescope.infra.exceptions.HttpResponseWrapperException;
import org.gooru.rescope.infra.services.RescopeApplicableService;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 17/5/18.
 */
class FetchRescopedContentService {

    private final DBI dbi;

    FetchRescopedContentService(DBI dbi) {
        this.dbi = dbi;
    }

    String fetchRescopedContent(FetchRescopedContentCommand command) {
        FetchRescopedContentDao dao = dbi.onDemand(FetchRescopedContentDao.class);

        if (command.getClassId() != null) {
            if (RescopeApplicableService.isRescopeApplicableToClass(command.getClassId())) {
                return dao.fetchRescopedContentForUserInClass(command.asBean());
            }
        } else {
            if (RescopeApplicableService.isRescopeApplicableToCourseInIL(command.getCourseId())) {
                return dao.fetchRescopedContentForUserInIL(command.asBean());
            }
        }
        throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
            "Rescope not applicable to specified course/class");
    }
}
