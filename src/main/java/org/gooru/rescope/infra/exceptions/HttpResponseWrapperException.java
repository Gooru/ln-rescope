package org.gooru.rescope.infra.exceptions;


import org.gooru.rescope.infra.constants.Constants;
import org.gooru.rescope.infra.constants.HttpConstants;

import io.vertx.core.json.JsonObject;

/**
 * @author ashish.
 */
public final class HttpResponseWrapperException extends RuntimeException {
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
