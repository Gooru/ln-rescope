package org.gooru.rescope.processors;

import io.vertx.core.Future;
import org.gooru.rescope.responses.MessageResponse;

/**
 * @author ashish.
 */
public interface AsyncMessageProcessor {

  Future<MessageResponse> process();

}
