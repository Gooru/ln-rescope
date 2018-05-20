package org.gooru.rescope.infra.services;

import org.skife.jdbi.v2.DBI;

/**
 * @author ashish on 20/5/18.
 */
class RescopeQueueInitializerServiceImpl implements RescopeQueueInitializerService {

    private final DBI dbi;

    RescopeQueueInitializerServiceImpl(DBI dbi) {
        this.dbi = dbi;
    }

    @Override
    public void initializeQueue() {
        RescopeRequestQueueDao dao = dbi.onDemand(RescopeRequestQueueDao.class);
        dao.initializeQueueStatus();
    }
}
