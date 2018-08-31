package org.gooru.rescope.routes;

import org.gooru.rescope.infra.constants.Constants;
import org.gooru.rescope.infra.constants.HttpConstants;
import org.gooru.rescope.routes.utils.DeliveryOptionsBuilder;
import org.gooru.rescope.routes.utils.RouteRequestUtility;
import org.gooru.rescope.routes.utils.RouteResponseUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * @author ashish
 */
public class RouteRescopeConfigurator implements RouteConfigurator {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteRescopeConfigurator.class);
    private EventBus eb = null;
    private long mbusTimeout;

    @Override
    public void configureRoutes(Vertx vertx, Router router, JsonObject config) {
        eb = vertx.eventBus();
        mbusTimeout = config.getLong(Constants.EventBus.MBUS_TIMEOUT, 30L) * 1000;
        router.get(Constants.Route.API_RESCOPE_FETCH).handler(this::fetchRescopedContent);
        router.post(Constants.Route.API_RESCOPE_CALCULATE).handler(this::doRescopeOfContent);
    }

    private void fetchRescopedContent(RoutingContext routingContext) {
        DeliveryOptions options = DeliveryOptionsBuilder.buildWithApiVersion(routingContext).setSendTimeout(mbusTimeout)
                .addHeader(Constants.Message.MSG_OP, Constants.Message.MSG_OP_RESCOPE_GET);
        eb.<JsonObject>send(Constants.EventBus.MBEP_RESCOPE, RouteRequestUtility.getBodyForMessage(routingContext),
                options, reply -> RouteResponseUtility.responseHandler(routingContext, reply, LOGGER));
    }

    private void doRescopeOfContent(RoutingContext routingContext) {
        DeliveryOptions options = DeliveryOptionsBuilder.buildWithApiVersion(routingContext).setSendTimeout(mbusTimeout)
                .addHeader(Constants.Message.MSG_OP, Constants.Message.MSG_OP_RESCOPE_SET);
        eb.<JsonObject>send(Constants.EventBus.MBEP_RESCOPE, RouteRequestUtility.getBodyForMessage(routingContext),
                options);
        RouteResponseUtility.responseHandlerStatusOnlyNoBodyOrHeaders(routingContext, HttpConstants.HttpStatus.SUCCESS);
    }
}
