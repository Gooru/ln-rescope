package org.gooru.rescope.processors.fetchrescopedcontent;

import org.gooru.rescope.infra.data.EventBusMessage;
import org.gooru.rescope.infra.jdbi.DBICreator;
import org.gooru.rescope.processors.AsyncMessageProcessor;
import org.gooru.rescope.responses.MessageResponse;
import org.gooru.rescope.responses.MessageResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 17/5/18.
 */
public class FetchRescopedContentProcessor implements AsyncMessageProcessor {

    private final Message<JsonObject> message;
    private final Vertx vertx;
    private final Future<MessageResponse> result;
    private EventBusMessage eventBusMessage;
    private static final Logger LOGGER = LoggerFactory.getLogger(FetchRescopedContentProcessor.class);
    private final FetchRescopedContentService fetchRescopedContentService =
        new FetchRescopedContentService(DBICreator.getDbiForDefaultDS());

    public FetchRescopedContentProcessor(Vertx vertx, Message<JsonObject> message) {
        this.message = message;
        this.vertx = vertx;
        this.result = Future.future();
    }

    @Override
    public Future<MessageResponse> process() {
        vertx.<MessageResponse>executeBlocking(future -> {
            try {
                this.eventBusMessage = EventBusMessage.eventBusMessageBuilder(message);

                FetchRescopedContentCommand command = FetchRescopedContentCommand.builder(eventBusMessage);
                String rescopedContent = fetchRescopedContentService.fetchRescopedContent(command);
                future.complete(createResponse(rescopedContent));
            } catch (Throwable throwable) {
                LOGGER.warn("Encountered exception", throwable);
                future.fail(throwable);
            }
        }, asyncResult -> {
            if (asyncResult.succeeded()) {
                result.complete(asyncResult.result());
            } else {
                result.fail(asyncResult.cause());
            }
        });
        return result;
    }

    private MessageResponse createResponse(String rescopedContent) {
        if (rescopedContent == null) {
            // TODO: Need to relay the event so that processing is queued
            return MessageResponseFactory.createNotFoundResponse("Rescoped content not found");
        }
        return MessageResponseFactory.createOkayResponse(new JsonObject(rescopedContent));
    }

}
