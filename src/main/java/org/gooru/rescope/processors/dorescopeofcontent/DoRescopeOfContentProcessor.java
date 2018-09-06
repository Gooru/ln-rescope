package org.gooru.rescope.processors.dorescopeofcontent;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.gooru.rescope.infra.data.EventBusMessage;
import org.gooru.rescope.infra.jdbi.DBICreator;
import org.gooru.rescope.processors.AsyncMessageProcessor;
import org.gooru.rescope.responses.MessageResponse;
import org.gooru.rescope.responses.MessageResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 17/5/18.
 */
public class DoRescopeOfContentProcessor implements AsyncMessageProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoRescopeOfContentProcessor.class);

    private final Message<JsonObject> message;
    private final Vertx vertx;
    private final Future<MessageResponse> result;
    private EventBusMessage eventBusMessage;

    private final DoRescopeOfContentService doRescopeOfContentService =
        new DoRescopeOfContentService(DBICreator.getDbiForDefaultDS());

    public DoRescopeOfContentProcessor(Vertx vertx, Message<JsonObject> message) {
        this.vertx = vertx;
        this.message = message;
        this.result = Future.future();
    }

    @Override
    public Future<MessageResponse> process() {
        vertx.<MessageResponse>executeBlocking(future -> {
            try {
                this.eventBusMessage = EventBusMessage.eventBusMessageBuilder(message);
                DoRescopeOfContentCommand command = DoRescopeOfContentCommand.builder(eventBusMessage.getRequestBody());
                doRescopeOfContentService.doRescope(command);
                future.complete(createResponse());
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

    private MessageResponse createResponse() {
        return MessageResponseFactory.createOkayResponse(new JsonObject());
    }

}
