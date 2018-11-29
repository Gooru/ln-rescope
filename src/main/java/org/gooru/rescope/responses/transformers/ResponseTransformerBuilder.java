package org.gooru.rescope.responses.transformers;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.gooru.rescope.infra.exceptions.HttpResponseWrapperException;

/**
 * @author ashish.
 */
public final class ResponseTransformerBuilder {

  public static ResponseTransformer build(Message<JsonObject> message) {
    return new HttpResponseTransformer(message);
  }

  public static ResponseTransformer buildHttpResponseWrapperExceptionBuild(
      HttpResponseWrapperException ex) {
    return new HttpResponseWrapperExceptionTransformer(ex);
  }

  private ResponseTransformerBuilder() {
    throw new AssertionError();
  }
}
