package org.gooru.rescope.routes;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * @author ashish.
 */
class RouteGlobalConfigurator implements RouteConfigurator {

  @Override
  public void configureRoutes(Vertx vertx, Router router, JsonObject config) {

    final long maxSizeInMb = config.getLong("request.body.size.max.mb", 5L);

    BodyHandler bodyHandler = BodyHandler.create().setBodyLimit(maxSizeInMb * 1_024 * 1_024);

    router.route().handler(bodyHandler);

  }

}
