package org.gooru.rescope.infra.services;

import org.gooru.rescope.infra.data.RescopeQueueModel;
import org.gooru.rescope.infra.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 20/5/18.
 */
public interface RescopeProcessingService {

    void doRescope(RescopeQueueModel model);

    static RescopeProcessingService build() {
        return new RescopeProcessingServiceImpl(DBICreator.getDbiForDefaultDS());
    }

    static RescopeProcessingService build(DBI dbi) {
        return new RescopeProcessingServiceImpl(dbi);
    }

}
