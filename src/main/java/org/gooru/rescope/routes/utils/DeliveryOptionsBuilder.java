package org.gooru.rescope.routes.utils;

import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.ext.web.RoutingContext;
import org.gooru.rescope.infra.constants.Constants;


/**
 * @author ashish
 */
public final class DeliveryOptionsBuilder {
    private DeliveryOptionsBuilder() {
        throw new AssertionError();
    }

    public static DeliveryOptions buildWithApiVersion(RoutingContext context) {
        final String apiVersion = context.request().getParam("version");
        VersionValidatorUtility.validateVersion(apiVersion);
        return new DeliveryOptions().addHeader(Constants.Message.MSG_API_VERSION, apiVersion);
    }
}
