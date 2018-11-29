package org.gooru.rescope.bootstrap.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.gooru.rescope.infra.constants.Constants;
import org.gooru.rescope.infra.exceptions.HttpResponseWrapperException;
import org.gooru.rescope.infra.exceptions.MessageResponseWrapperException;
import org.gooru.rescope.processors.ProcessorBuilder;
import org.gooru.rescope.responses.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish.
 */
public class RescopeVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(RescopeVerticle.class);

  private static void futureResultHandler(Message<JsonObject> message,
      Future<MessageResponse> future,
      boolean replyNeeded) {
    future.setHandler(event -> {
      if (event.succeeded() && replyNeeded) {
        message.reply(event.result().reply(), event.result().deliveryOptions());
      } else if (replyNeeded) {
        LOGGER.warn("Failed to process command", event.cause());
        if (event.cause() instanceof HttpResponseWrapperException) {
          HttpResponseWrapperException exception = (HttpResponseWrapperException) event.cause();
          message
              .reply(new JsonObject().put(Constants.Message.MSG_HTTP_STATUS, exception.getStatus())
                  .put(Constants.Message.MSG_HTTP_BODY, exception.getBody())
                  .put(Constants.Message.MSG_HTTP_HEADERS, new JsonObject()));
        } else if (event.cause() instanceof MessageResponseWrapperException) {
          MessageResponseWrapperException exception = (MessageResponseWrapperException) event
              .cause();
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
  public void start(Future<Void> startFuture) {

    EventBus eb = vertx.eventBus();
    eb.localConsumer(Constants.EventBus.MBEP_RESCOPE, this::processMessage)
        .completionHandler(result -> {
          if (result.succeeded()) {
            LOGGER.info("Rescope end point ready to listen");
            startFuture.complete();
          } else {
            LOGGER.error("Error registering the Rescope handler. Halting the machinery");
            startFuture.fail(result.cause());
            Runtime.getRuntime().halt(1);
          }
        });
  }

  @Override
  public void stop(Future<Void> stopFuture) {
  }

  private void processMessage(Message<JsonObject> message) {
    String op = message.headers().get(Constants.Message.MSG_OP);
    Future<MessageResponse> future;
    boolean replyNeeded = true;
    switch (op) {
      case Constants.Message.MSG_OP_RESCOPE_GET:
        future = ProcessorBuilder.buildFetchRescopedContentProcessor(vertx, message).process();
        break;
      case Constants.Message.MSG_OP_RESCOPE_SET:
        future = ProcessorBuilder.buildDoRescopeOfContentProcessor(vertx, message).process();
        replyNeeded = false;
        break;
      default:
        LOGGER.warn("Invalid operation type");
        future = ProcessorBuilder.buildPlaceHolderExceptionProcessor(vertx, message).process();
    }

    futureResultHandler(message, future, replyNeeded);
  }
}
