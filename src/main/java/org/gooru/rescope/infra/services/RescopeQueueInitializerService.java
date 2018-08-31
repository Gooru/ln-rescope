package org.gooru.rescope.infra.services;

import org.gooru.rescope.infra.jdbi.DBICreator;

/**
 * This service will be used once at the start of application. This service will
 * mark all the record in DB queue which are marked as either dispatched or in
 * process, to queued state. This is to handle cases where some records were
 * being processed while the system shut down, and thus those record need to be
 * reprocessed.
 *
 * @author ashish on 20/5/18.
 */
public interface RescopeQueueInitializerService {
    void initializeQueue();

    static RescopeQueueInitializerService build() {
        return new RescopeQueueInitializerServiceImpl(DBICreator.getDbiForDefaultDS());
    }
}
