package org.gooru.rescope.processors;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;
import org.gooru.rescope.processors.dorescopeofcontent.DoRescopeOfContentProcessor;
import org.gooru.rescope.processors.fetchrescopedcontent.FetchRescopedContentProcessor;
import org.gooru.rescope.processors.learnerprofilebaselineprocessor.LearnerProfileBaselineProcessor;
import org.gooru.rescope.responses.MessageResponse;

/**
 * @author ashish on 17/5/18.
 */
public final class ProcessorBuilder {

  public static AsyncMessageProcessor buildFetchRescopedContentProcessor(Vertx vertx,
      Message<JsonObject> message) {
    return new FetchRescopedContentProcessor(vertx, message);
  }

  public static AsyncMessageProcessor buildDoRescopeOfContentProcessor(Vertx vertx,
      Message<JsonObject> message) {
    return new DoRescopeOfContentProcessor(vertx, message);
  }

  public static AsyncMessageProcessor buildPlaceHolderExceptionProcessor(Vertx vertx,
      Message<JsonObject> message) {
    return () -> {
      Future<MessageResponse> future = Future.future();
      future.fail(new IllegalStateException("Illegal State for processing command"));
      return future;
    };
  }

  public static AsyncMessageProcessor buildLPBaselineProcessor(Vertx vertx,
      Message<JsonObject> message,
      HttpClient client, String lpbaselineUri) {
    return new LearnerProfileBaselineProcessor(vertx, message, client, lpbaselineUri);
  }

  private ProcessorBuilder() {
  }
}
