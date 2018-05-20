package org.gooru.rescope.bootstrap.verticles;

import org.gooru.rescope.infra.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

/**
 * @author ashish.
 */
public class RescopeProcessingVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(RescopeProcessingVerticle.class);

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

    private void processMessage(Message<String> message) {
        String payload = message.body();
        LOGGER.debug("Payload received is: '{}'", payload);
        LOGGER.debug("Will send empty reply");
        message.reply("");
    }

    @Override
    public void stop(Future<Void> stopFuture) {
    }
}
