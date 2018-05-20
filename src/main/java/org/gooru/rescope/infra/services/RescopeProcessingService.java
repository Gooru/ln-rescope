package org.gooru.rescope.infra.services;

import org.gooru.rescope.infra.data.RescopeQueueModel;
import org.gooru.rescope.infra.jdbi.DBICreator;

/**
 * @author ashish on 20/5/18.
 */
public interface RescopeProcessingService {

    void doRescope(RescopeQueueModel model);

    static RescopeProcessingService build() {
        return new RescopeProcessingServiceImpl(DBICreator.getDbiForDefaultDS());
    }
}
