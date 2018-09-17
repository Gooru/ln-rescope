package org.gooru.rescope.infra.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gooru.rescope.infra.data.RescopeQueueModel;
import org.gooru.rescope.infra.services.core.RescopeProcessor;
import org.gooru.rescope.infra.services.core.RescopeProcessorContext;
import org.gooru.rescope.infra.services.itemfilter.SkippedItemsResponse;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 20/5/18.
 */
class RescopeQueueRecordProcessingServiceImpl implements RescopeQueueRecordProcessingService {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(RescopeQueueRecordProcessingService.class);

  private final DBI dbi;
  private RescopeQueueModel model;
  private RescopeQueueRecordProcessingServiceDao dao;

  RescopeQueueRecordProcessingServiceImpl(DBI dbi) {
    this.dbi = dbi;
  }

  @Override
  public void doRescope(RescopeQueueModel model) {
    this.model = model;
    this.dao = dbi.onDemand(RescopeQueueRecordProcessingServiceDao.class);
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
      SkippedItemsResponse items = RescopeProcessor.buildRescopeProcessor()
          .rescopedItems(RescopeProcessorContext.buildFromRescopeQueueModel(model));
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
