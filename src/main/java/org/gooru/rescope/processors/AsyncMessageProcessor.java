package org.gooru.rescope.processors;

import org.gooru.rescope.responses.MessageResponse;
import org.gooru.rescope.responses.MessageResponseFactory;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 17/11/17.
 */
public interface AsyncMessageProcessor {

    Future<MessageResponse> process();

    static AsyncMessageProcessor buildPlaceHolderSuccessProcessor(Vertx vertx, Message<JsonObject> message) {
        return () -> {
            Future<MessageResponse> future = Future.future();
            future.complete(MessageResponse.Builder.buildPlaceHolderResponse());
            return future;
        };
    }

    static AsyncMessageProcessor buildHttp404Processor(Vertx vertx, Message<JsonObject> message) {
        return () -> {
            Future<MessageResponse> future = Future.future();
            future.complete(MessageResponseFactory.createNotFoundResponse("Rescope data not found"));
            return future;
        };
    }

    static AsyncMessageProcessor buildPlaceHolderExceptionProcessor(Vertx vertx, Message<JsonObject> message) {
        return () -> {
            Future<MessageResponse> future = Future.future();
            future.fail(new IllegalStateException("Illegal State for processing command"));
            return future;
        };
    }

}
