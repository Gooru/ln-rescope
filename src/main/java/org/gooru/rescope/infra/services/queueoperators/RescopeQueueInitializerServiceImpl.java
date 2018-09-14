package org.gooru.rescope.infra.services.queueoperators;

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
        RescopeQueueOperatorDao dao = dbi.onDemand(RescopeQueueOperatorDao.class);
        dao.initializeQueueStatus();
    }
}
