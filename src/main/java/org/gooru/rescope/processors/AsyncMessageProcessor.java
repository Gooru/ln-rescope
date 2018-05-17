package org.gooru.rescope.processors;

import org.gooru.rescope.responses.MessageResponse;

import io.vertx.core.Future;

/**
 * @author ashish.
 */
public interface AsyncMessageProcessor {

    Future<MessageResponse> process();

}
