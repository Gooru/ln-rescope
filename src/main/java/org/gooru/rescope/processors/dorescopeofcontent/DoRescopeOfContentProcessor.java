package org.gooru.rescope.processors.dorescopeofcontent;

import org.gooru.rescope.infra.data.EventBusMessage;
import org.gooru.rescope.processors.AsyncMessageProcessor;
import org.gooru.rescope.responses.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 17/5/18.
 */
public class DoRescopeOfContentProcessor implements AsyncMessageProcessor {
    private final Message<JsonObject> message;
    private final Vertx vertx;
    private final Future<MessageResponse> result;
    private EventBusMessage eventBusMessage;
    private static final Logger LOGGER = LoggerFactory.getLogger(DoRescopeOfContentProcessor.class);
/*
    private final DoRescopeOfContentService doRescopeOfContentService =
        new DoRescopeOfContentService(DBICreator.getDbiForDefaultDS());
*/

    public DoRescopeOfContentProcessor(Vertx vertx, Message<JsonObject> message) {
        this.vertx = vertx;
        this.message = message;
        this.result = Future.future();
    }

    @Override
    public Future<MessageResponse> process() {
        try {
            this.eventBusMessage = EventBusMessage.eventBusMessageBuilder(message);
            throw new IllegalStateException("Not implemented");
/*
            DoRescopeOfContentCommand command =
                DoRescopeOfContentCommand.builder(eventBusMessage.getRequestBody());
            doRescopeOfContentService.doRescope(command);
*/
        } catch (Throwable throwable) {
            LOGGER.warn("Encountered exception", throwable);
            result.fail(throwable);
        }
        return result;
    }

}
