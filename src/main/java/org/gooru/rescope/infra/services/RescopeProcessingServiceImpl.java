package org.gooru.rescope.infra.services;

import org.gooru.rescope.infra.data.RescopeQueueModel;
import org.gooru.rescope.infra.services.itemfilter.SkippedItemsFinder;
import org.gooru.rescope.infra.services.itemfilter.SkippedItemsResponse;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author ashish on 20/5/18.
 */
class RescopeProcessingServiceImpl implements RescopeProcessingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RescopeProcessingService.class);

    private final DBI dbi;
    private RescopeQueueModel model;
    private RescopeProcessingDao dao;

    RescopeProcessingServiceImpl(DBI dbi) {
        this.dbi = dbi;
    }

    @Override
    public void doRescope(RescopeQueueModel model) {
        this.model = model;
        this.dao = dbi.onDemand(RescopeProcessingDao.class);
        if (!recordIsStillInDispatchedState()) {
            LOGGER.debug("Record is not found to be in dispatched state");
            return;
        }
        if (wasRescopeAlreadyDone()) {
            LOGGER.debug("Rescope was already done");
            dequeueRecord();
            return;
        }
        processRecord();
    }

    private void dequeueRecord() {
        LOGGER.debug("Dequeueing record");
        dao.dequeueRecord(model.getId());
    }

    private void processRecord() {
        LOGGER.debug("Doing real processing");
        try {
            SkippedItemsResponse items = SkippedItemsFinder.buildSkippedItemsFinderForCourse()
                .findItemsThatWillBeSkipped(model.getUserId(), model.getCourseId());
            ObjectMapper mapper = new ObjectMapper();
            try {
                String skippedItemsString = mapper.writeValueAsString(items);
                dao.persistRescopedContent(model, skippedItemsString);
            } catch (JsonProcessingException e) {
                LOGGER.warn("Not able to convert skipped items to JSON for model '{}'", model.toJson(), e);
            }
        } catch (Exception e) {
            LOGGER.warn("Not able to do rescope for model: '{}'. Will dequeue record.", e);
            throw e;
        } finally {
            dequeueRecord();
        }
    }

    private boolean wasRescopeAlreadyDone() {
        if (model.getClassId() == null) {
            return dao.rescopeDoneForUserInIL(model);
        }
        return dao.rescopeDoneForUserInClass(model);
    }

    private boolean recordIsStillInDispatchedState() {
        return dao.isQueuedRecordStillDispatched(model.getId());
    }
}
