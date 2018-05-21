package org.gooru.rescope.infra.services;

import org.gooru.rescope.infra.data.RescopeQueueModel;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 20/5/18.
 */
class RescopeProcessingServiceImpl implements RescopeProcessingService {
    private final DBI dbi;
    private RescopeQueueModel model;
    private RescopeRequestQueueDao dao;
    private static final Logger LOGGER = LoggerFactory.getLogger(RescopeProcessingService.class);

    RescopeProcessingServiceImpl(DBI dbi) {
        this.dbi = dbi;
    }

    @Override
    public void doRescope(RescopeQueueModel model) {
        this.model = model;
        this.dao = dbi.onDemand(RescopeRequestQueueDao.class);
        if (!recordIsStillInDispatchedState()) {
            LOGGER.debug("Record is not found to be in dispatched state");
            return;
        }
        if (rescopeWasAlreadyDone()) {
            LOGGER.debug("Rescope was already done");
            dequeueRecord();
            return;
        }
        processRecord();
        dequeueRecord();
    }

    private void dequeueRecord() {
        LOGGER.debug("Dequeueing record");
        dao.dequeueRecord(model.getId());
    }

    private void processRecord() {
        // TODO : Provide implementation
        LOGGER.debug("Doing real processing");
        //        throw new IllegalStateException("Not implemented");
    }

    private boolean rescopeWasAlreadyDone() {
        if (model.getClassId() == null) {
            return dao.rescopeDoneForUserInIL(model);
        }
        return dao.rescopeDoneForUserInClass(model);
    }

    private boolean recordIsStillInDispatchedState() {
        return dao.isQueuedRecordStillDispatched(model.getId());
    }
}
