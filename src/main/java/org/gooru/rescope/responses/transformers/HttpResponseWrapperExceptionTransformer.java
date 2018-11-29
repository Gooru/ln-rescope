package org.gooru.rescope.responses.transformers;

import io.vertx.core.json.JsonObject;
import java.util.Collections;
import java.util.Map;
import org.gooru.rescope.infra.exceptions.HttpResponseWrapperException;

/**
 * @author ashish.
 */
public final class HttpResponseWrapperExceptionTransformer implements ResponseTransformer {

  private final HttpResponseWrapperException ex;

  HttpResponseWrapperExceptionTransformer(HttpResponseWrapperException ex) {
    this.ex = ex;
  }

  @Override
  public void transform() {
    // no op
  }

  @Override
  public JsonObject transformedBody() {
    return ex.getBody();
  }

  @Override
  public Map<String, String> transformedHeaders() {
    return Collections.emptyMap();
  }

  @Override
  public int transformedStatus() {
    return ex.getStatus();
  }
}
