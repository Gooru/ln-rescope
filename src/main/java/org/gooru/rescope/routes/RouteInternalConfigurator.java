package org.gooru.rescope.routes;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.gooru.rescope.infra.constants.Constants;
import org.gooru.rescope.infra.constants.HttpConstants;
import org.gooru.rescope.routes.utils.DeliveryOptionsBuilder;
import org.gooru.rescope.routes.utils.RouteRequestUtility;
import org.gooru.rescope.routes.utils.RouteResponseUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish.
 */
class RouteInternalConfigurator implements RouteConfigurator {

  private static final Logger LOGGER = LoggerFactory.getLogger(RouteInternalConfigurator.class);
  private EventBus eb = null;
  private long mbusTimeout;

  @Override
  public void configureRoutes(Vertx vertx, Router router, JsonObject config) {
    LOGGER.debug("Configuring routes for internal route");
    eb = vertx.eventBus();
    mbusTimeout = config.getLong(Constants.EventBus.MBUS_TIMEOUT, 30L) * 1_000;
    router.post(Constants.Route.API_INTERNAL_RESCOPE_CALCULATE).handler(this::doRescopeOfContent);
  }

  private void doRescopeOfContent(RoutingContext routingContext) {
    DeliveryOptions options = DeliveryOptionsBuilder
        .buildWithoutApiVersion(routingContext, mbusTimeout, Constants.Message.MSG_OP_RESCOPE_SET);
    eb.send(Constants.EventBus.MBEP_RESCOPE, RouteRequestUtility.getBodyForMessage(routingContext),
        options);
    RouteResponseUtility
        .responseHandlerStatusOnlyNoBodyOrHeaders(routingContext, HttpConstants.HttpStatus.SUCCESS);
  }

}
