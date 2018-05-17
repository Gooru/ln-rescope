package org.gooru.rescope.bootstrap.verticles;

import java.util.Random;

import org.gooru.rescope.infra.constants.Constants;
import org.gooru.rescope.infra.exceptions.HttpResponseWrapperException;
import org.gooru.rescope.infra.exceptions.MessageResponseWrapperException;
import org.gooru.rescope.processors.AsyncMessageProcessor;
import org.gooru.rescope.responses.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish.
 */
public class RescopeVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(RescopeVerticle.class);

    @Override
    public void start(Future<Void> startFuture) {

        EventBus eb = vertx.eventBus();
        eb.localConsumer(Constants.EventBus.MBEP_RESCOPE, this::processMessage).completionHandler(result -> {
            if (result.succeeded()) {
                LOGGER.info("Content end point ready to listen");
                startFuture.complete();
            } else {
                LOGGER.error("Error registering the Content handler. Halting the machinery");
                startFuture.fail(result.cause());
                Runtime.getRuntime().halt(1);
            }
        });
    }

    private void processMessage(Message<JsonObject> message) {
        String op = message.headers().get(Constants.Message.MSG_OP);
        Future<MessageResponse> future;
        switch (op) {
        case Constants.Message.MSG_OP_RESCOPE_GET:
            future = getStubbedResponse(message);
            break;
        case Constants.Message.MSG_OP_RESCOPE_SET:
            future = AsyncMessageProcessor.buildPlaceHolderSuccessProcessor(vertx, message).process();
            break;
        default:
            LOGGER.warn("Invalid operation type");
            future = AsyncMessageProcessor.buildPlaceHolderExceptionProcessor(vertx, message).process();
        }

        futureResultHandler(message, future);
    }

    private static final Random random = new Random();

    private Future<MessageResponse> getStubbedResponse(Message<JsonObject> message) {
        if (random.nextBoolean()) {
            return AsyncMessageProcessor.buildDummyProcessor(vertx, message).process();
        } else {
            return AsyncMessageProcessor.buildHttp404Processor(vertx, message).process();
        }
    }

    private static void futureResultHandler(Message<JsonObject> message, Future<MessageResponse> future) {
        future.setHandler(event -> {
            if (event.succeeded()) {
                message.reply(event.result().reply(), event.result().deliveryOptions());
            } else {
                LOGGER.warn("Failed to process next command", event.cause());
                if (event.cause() instanceof HttpResponseWrapperException) {
                    HttpResponseWrapperException exception = (HttpResponseWrapperException) event.cause();
                    message.reply(new JsonObject().put(Constants.Message.MSG_HTTP_STATUS, exception.getStatus())
                        .put(Constants.Message.MSG_HTTP_BODY, exception.getBody())
                        .put(Constants.Message.MSG_HTTP_HEADERS, new JsonObject()));
                } else if (event.cause() instanceof MessageResponseWrapperException) {
                    MessageResponseWrapperException exception = (MessageResponseWrapperException) event.cause();
                    message.reply(exception.getMessageResponse().reply(),
                        exception.getMessageResponse().deliveryOptions());
                } else {
                    message.reply(new JsonObject().put(Constants.Message.MSG_HTTP_STATUS, 500)
                        .put(Constants.Message.MSG_HTTP_BODY, new JsonObject())
                        .put(Constants.Message.MSG_HTTP_HEADERS, new JsonObject()));
                }
            }
        });
    }

    @Override
    public void stop(Future<Void> stopFuture) {
    }
}
