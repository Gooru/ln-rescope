package org.gooru.rescope.routes;

import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.gooru.rescope.infra.constants.Constants;
import org.gooru.rescope.infra.constants.HttpConstants;
import org.gooru.rescope.infra.utils.TokenValidationUtils;
import org.gooru.rescope.responses.auth.AuthSessionResponseHolder;
import org.gooru.rescope.responses.auth.AuthSessionResponseHolderBuilder;
import org.gooru.rescope.routes.utils.DeliveryOptionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish.
 */
class RouteAuthConfigurator implements RouteConfigurator {

  private static final Logger LOGGER = LoggerFactory.getLogger(RouteAuthConfigurator.class);
  private EventBus eBus;
  private long mbusTimeout;

  @Override
  public void configureRoutes(Vertx vertx, Router router, JsonObject config) {
    this.eBus = vertx.eventBus();
    this.mbusTimeout = config.getLong(Constants.EventBus.MBUS_TIMEOUT, 30L);

    router.route(Constants.Route.API_AUTH_ROUTE).handler(this::verifyToken);
  }

  private void verifyToken(RoutingContext routingContext) {
    String sessionToken = TokenValidationUtils
        .extractSessionToken(routingContext.request().getHeader(HttpConstants.HEADER_AUTH));

    if (sessionToken == null || sessionToken.isEmpty()) {
      this.sendUnAuthorizedResponse(routingContext);
    } else {
      routingContext.put(Constants.Message.MSG_SESSION_TOKEN, sessionToken);
      this.eBus.<JsonObject>send(Constants.EventBus.MBEP_AUTH, null,
          this.createDeliveryOptionsForTokenVerification(routingContext, sessionToken),
          reply -> this.tokenVerificationCompletionHandler(routingContext, reply));
    }
  }

  private void tokenVerificationCompletionHandler(RoutingContext routingContext,
      AsyncResult<Message<JsonObject>> reply) {
    if (reply.succeeded()) {
      AuthSessionResponseHolder responseHolder = AuthSessionResponseHolderBuilder
          .build(reply.result());

      if (responseHolder.isAuthorized() && !responseHolder.isAnonymous()) {
        JsonObject session = responseHolder.getSession();
        setupSessionInRoutingContext(routingContext, session, responseHolder.getUser());
        routingContext.next();
      } else {
        this.logUnAuthorized(responseHolder);
        this.sendUnAuthorizedResponse(routingContext);
      }
    } else {
      LOGGER.error("Not able to send message to Token verification endpoint", reply.cause());
      routingContext.response().setStatusCode(HttpConstants.HttpStatus.ERROR.getCode()).end();
    }
  }

  private void setupSessionInRoutingContext(RoutingContext routingContext, JsonObject session,
      String user) {
    routingContext.put(Constants.Message.MSG_KEY_SESSION, session)
        .put(Constants.Message.MSG_USER_ID, user);
  }

  private void logUnAuthorized(AuthSessionResponseHolder responseHolder) {
    if (responseHolder.isAuthorized()) {
      LOGGER.warn("Anonymous access not allowed");
    } else {
      LOGGER.warn("Unauthorized access not allowed");
    }
  }

  private DeliveryOptions createDeliveryOptionsForTokenVerification(RoutingContext routingContext,
      String sessionToken) {
    return DeliveryOptionsBuilder
        .buildWithoutApiVersion(routingContext, this.mbusTimeout, Constants.Message.MSG_OP_AUTH)
        .addHeader(Constants.Message.MSG_SESSION_TOKEN, sessionToken);
  }

  private void sendUnAuthorizedResponse(RoutingContext routingContext) {
    routingContext.response().setStatusCode(HttpConstants.HttpStatus.UNAUTHORIZED.getCode())
        .setStatusMessage(HttpConstants.HttpStatus.UNAUTHORIZED.getMessage()).end();
  }

}
