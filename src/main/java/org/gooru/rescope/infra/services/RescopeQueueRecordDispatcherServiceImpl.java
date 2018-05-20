package org.gooru.rescope.infra.services;

import org.gooru.rescope.infra.data.RescopeQueueModel;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 20/5/18.
 */
class RescopeQueueRecordDispatcherServiceImpl implements RescopeQueueRecordDispatcherService {

    private final DBI dbi;
    private static final Logger LOGGER = LoggerFactory.getLogger(RescopeQueueRecordDispatcherService.class);

    RescopeQueueRecordDispatcherServiceImpl(DBI dbi) {
        this.dbi = dbi;
    }

    @Override
    public RescopeQueueModel getNextRecordToDispatch() {
        RescopeRequestQueueDao dao = dbi.onDemand(RescopeRequestQueueDao.class);
        RescopeQueueModel model = dao.getNextDispatchableModel();
        if (model == null) {
            LOGGER.debug("No records present for processing");
            model = RescopeQueueModel.createNonPersistedEmptyModel();
        } else {
            dao.setQueuedRecordStatusAsDispatched(model.getId());
        }
        return model;
    }
}
