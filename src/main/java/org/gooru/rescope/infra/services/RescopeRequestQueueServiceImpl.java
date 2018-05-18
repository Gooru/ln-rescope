package org.gooru.rescope.infra.services;

import org.gooru.rescope.infra.data.RescopeContext;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 18/5/18.
 */
class RescopeRequestQueueServiceImpl implements RescopeRequestQueueService {

    private final DBI dbi;

    RescopeRequestQueueServiceImpl(DBI dbi) {
        this.dbi = dbi;
    }

    @Override
    public void enqueue(RescopeContext context) {
        System.out.println("Received request for queueing");
        System.out.println(context.toString());
    }
}
