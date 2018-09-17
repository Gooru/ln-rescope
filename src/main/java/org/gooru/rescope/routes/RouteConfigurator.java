package org.gooru.rescope.routes;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

/**
 * @author ashish.
 */
public interface RouteConfigurator {

  void configureRoutes(Vertx vertx, Router router, JsonObject config);
}
