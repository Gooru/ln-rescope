package org.gooru.rescope.responses.auth;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish
 */
public final class AuthSessionResponseHolderBuilder {

  public static AuthSessionResponseHolder build(Message<JsonObject> message) {
    return new AuthSessionMessageBusJsonResponseHolder(message);
  }

  private AuthSessionResponseHolderBuilder() {
    throw new AssertionError();
  }
}
