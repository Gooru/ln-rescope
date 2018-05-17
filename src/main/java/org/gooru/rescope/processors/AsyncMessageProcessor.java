package org.gooru.rescope.processors;

import java.util.UUID;

import org.gooru.rescope.infra.constants.Constants;
import org.gooru.rescope.responses.MessageResponse;
import org.gooru.rescope.responses.MessageResponseFactory;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish.
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

    static AsyncMessageProcessor buildDummyProcessor(Vertx vertx, Message<JsonObject> message) {
        return () -> {
            Future<MessageResponse> future = Future.future();
            JsonObject result = new JsonObject().put(Constants.Response.RESP_UNITS,
                new JsonArray().add(UUID.randomUUID().toString()).add(UUID.randomUUID().toString()))
                .put(Constants.Response.RESP_LESSONS,
                    new JsonArray().add(UUID.randomUUID().toString()).add(UUID.randomUUID().toString())
                        .add(UUID.randomUUID().toString())).put(Constants.Response.RESP_ASSESSMENT,
                    new JsonArray().add(UUID.randomUUID().toString()).add(UUID.randomUUID().toString())
                        .add(UUID.randomUUID().toString()).add(UUID.randomUUID().toString()))
                .put(Constants.Response.RESP_COLLECTIONS,
                    new JsonArray().add(UUID.randomUUID().toString()).add(UUID.randomUUID().toString())
                        .add(UUID.randomUUID().toString()).add(UUID.randomUUID().toString())
                        .add(UUID.randomUUID().toString())).put(Constants.Response.RESP_ASSESSMENT_EX,
                    new JsonArray().add(UUID.randomUUID().toString()).add(UUID.randomUUID().toString())
                        .add(UUID.randomUUID().toString()).add(UUID.randomUUID().toString())
                        .add(UUID.randomUUID().toString())).put(Constants.Response.RESP_COLLECTIONS_EX,
                    new JsonArray().add(UUID.randomUUID().toString()).add(UUID.randomUUID().toString())
                        .add(UUID.randomUUID().toString()).add(UUID.randomUUID().toString()));
            future.complete(MessageResponseFactory.createOkayResponse(result));
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
