package org.gooru.rescope.bootstrap.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import org.gooru.rescope.infra.constants.Constants;
import org.gooru.rescope.infra.data.RescopeQueueModel;
import org.gooru.rescope.infra.services.RescopeQueueRecordProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish.
 */
public class RescopeProcessingVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(RescopeProcessingVerticle.class);

  private static final String SUCCESS = "SUCCESS";
  private static final String FAIL = "FAIL";

  @Override
  public void start(Future<Void> startFuture) {

    EventBus eb = vertx.eventBus();
    eb.localConsumer(Constants.EventBus.MBEP_RESCOPE_QUEUE_PROCESSOR, this::processMessage)
        .completionHandler(result -> {
          if (result.succeeded()) {
            LOGGER.info("Rescope processor point ready to listen");
            startFuture.complete();
          } else {
            LOGGER.error("Error registering the Rescope processor handler. Halting the machinery");
            startFuture.fail(result.cause());
            Runtime.getRuntime().halt(1);
          }
        });
  }

  @Override
  public void stop(Future<Void> stopFuture) {
  }

  private void processMessage(Message<String> message) {
    vertx.executeBlocking(future -> {
      try {
        RescopeQueueModel model = RescopeQueueModel.fromJson(message.body());
        RescopeQueueRecordProcessingService.build().doRescope(model);
        future.complete();
      } catch (Exception e) {
        LOGGER.warn("Not able to rescope the model. '{}'", message.body(), e);
        future.fail(e);
      }
    }, asyncResult -> {
      if (asyncResult.succeeded()) {
        message.reply(SUCCESS);
      } else {
        LOGGER.warn("Rescoping not done for model: '{}'", message.body(), asyncResult.cause());
        message.reply(FAIL);
      }
    });
  }
}
