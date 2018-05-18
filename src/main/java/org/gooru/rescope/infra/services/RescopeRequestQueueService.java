package org.gooru.rescope.infra.services;

import org.gooru.rescope.infra.data.RescopeContext;
import org.gooru.rescope.infra.jdbi.DBICreator;

/**
 * @author ashish on 18/5/18.
 */
public interface RescopeRequestQueueService {
    void enqueue(RescopeContext context);

    static RescopeRequestQueueService build() {
        return new RescopeRequestQueueServiceImpl(DBICreator.getDbiForDefaultDS());
    }
}
