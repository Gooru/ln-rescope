package org.gooru.rescope.processors.fetchrescopedcontent;

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

        if (command.getClassId() == null) {
            return dao.fetchRescopedContentForUserInIL(command.asBean());
        } else {
            return dao.fetchRescopedContentForUserInClass(command.asBean());
        }
    }
}
