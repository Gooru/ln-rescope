package org.gooru.rescope.infra.services.rescoperequest;

import org.gooru.rescope.infra.data.RescopeContext;
import org.gooru.rescope.infra.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 18/5/18.
 */
public interface RescopeRequestQueueService {

    void enqueue(RescopeContext context);

    static RescopeRequestQueueService build() {
        return new RescopeRequestQueueServiceImpl(DBICreator.getDbiForDefaultDS());
    }

    static RescopeRequestQueueService build(DBI dbi) {
        return new RescopeRequestQueueServiceImpl(dbi);
    }

}
