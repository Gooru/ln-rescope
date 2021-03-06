package org.gooru.rescope.infra.exceptions;

import io.vertx.core.json.JsonObject;
import org.gooru.rescope.infra.constants.Constants;
import org.gooru.rescope.infra.constants.HttpConstants;

/**
 * @author ashish.
 */
public final class HttpResponseWrapperException extends RuntimeException {

  private static final long serialVersionUID = 1898021518233298246L;
  private final HttpConstants.HttpStatus status;
  private final JsonObject payload;

  public HttpResponseWrapperException(HttpConstants.HttpStatus status, JsonObject payload) {
    this.status = status;
    this.payload = payload;
  }

  public HttpResponseWrapperException(HttpConstants.HttpStatus status, String message) {
    super(message);
    this.status = status;
    this.payload = new JsonObject().put(Constants.Message.MSG_MESSAGE, message);
  }

  public int getStatus() {
    return this.status.getCode();
  }

  public JsonObject getBody() {
    return this.payload;
  }
}
