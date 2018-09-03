package org.gooru.rescope.routes;

import org.gooru.rescope.infra.constants.HttpConstants;
import org.gooru.rescope.infra.exceptions.HttpResponseWrapperException;
import org.gooru.rescope.responses.transformers.ResponseTransformerBuilder;
import org.gooru.rescope.responses.writers.ResponseWriterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * Created by ashish.
 */
class RouteFailureConfigurator implements RouteConfigurator {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteFailureConfigurator.class);

    private static void handleFailures(RoutingContext frc) {
        Throwable currentThrowable = frc.failure();
        if (currentThrowable instanceof io.vertx.core.json.DecodeException) {
            LOGGER.error("Caught registered exception", currentThrowable);
            frc.response().setStatusCode(HttpConstants.HttpStatus.BAD_REQUEST.getCode()).end("Invalid Json payload");
        } else if (currentThrowable instanceof HttpResponseWrapperException) {
            LOGGER.error("Caught HttpResponseWrapperException", currentThrowable);
            ResponseWriterBuilder
                .build(frc, ResponseTransformerBuilder
                    .buildHttpResponseWrapperExceptionBuild((HttpResponseWrapperException) currentThrowable))
                .writeResponse();
        } else {
            LOGGER.error("Caught unregistered exception, will send HTTP.500", currentThrowable);
            frc.response().setStatusCode(HttpConstants.HttpStatus.ERROR.getCode()).end("Internal error");
        }
    }

    @Override
    public void configureRoutes(Vertx vertx, Router router, JsonObject config) {

        router.put().failureHandler(RouteFailureConfigurator::handleFailures);
        router.post().failureHandler(RouteFailureConfigurator::handleFailures);
        router.get().failureHandler(RouteFailureConfigurator::handleFailures);
        router.delete().failureHandler(RouteFailureConfigurator::handleFailures);
    }

}
