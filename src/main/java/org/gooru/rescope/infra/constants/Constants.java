package org.gooru.rescope.infra.constants;

import java.util.UUID;

/**
 * @author ashish.
 */
public final class Constants {

    private Constants() {
        throw new AssertionError();
    }

    public static final class EventBus {

        public static final String MBEP_AUTH = "org.gooru.rescope.eventbus.auth";
        public static final String MBEP_RESCOPE = "org.gooru.rescope.eventbus.rescope";

        public static final String MBEP_RESCOPE_QUEUE_PROCESSOR = "org.gooru.rescope.eventbus.queueprocessor";
        public static final String MBEP_RESCOPE_POST_PROCESSOR = "org.gooru.rescope.eventbus.postprocessor";
        public static final String MBUS_TIMEOUT = "event.bus.send.timeout.seconds";

        private EventBus() {
            throw new AssertionError();
        }
    }

    public static final class Message {

        public static final String MSG_OP = "mb.operation";
        public static final String MSG_API_VERSION = "api.version";
        public static final String MSG_SESSION_TOKEN = "session.token";
        public static final String MSG_OP_AUTH = "auth";
        public static final String MSG_KEY_SESSION = "session";
        public static final String MSG_OP_STATUS = "mb.op.status";
        public static final String MSG_OP_STATUS_SUCCESS = "mb.op.status.success";
        public static final String MSG_OP_STATUS_FAIL = "mb.op.status.fail";
        public static final String MSG_USER_ANONYMOUS = "anonymous";
        public static final String MSG_USER_ID = "user_id";
        public static final String MSG_HTTP_STATUS = "http.status";
        public static final String MSG_HTTP_BODY = "http.body";
        public static final String MSG_HTTP_HEADERS = "http.headers";

        public static final String MSG_OP_RESCOPE_GET = "rescope.get";
        public static final String MSG_OP_RESCOPE_SET = "rescope.set";
        public static final String MSG_OP_RESCOPE_LP_BASELINE = "rescope.lpbaseline";
        public static final String MSG_MESSAGE = "message";
        public static final String ACCESS_TOKEN_VALIDITY = "access_token_validity";

        private Message() {
            throw new AssertionError();
        }
    }

    public static final class Response {

        private Response() {
            throw new AssertionError();
        }
    }

    public static final class Params {

        public static final String PARAM_MEMBER_ID = "member_id";
        public static final String PARAM_CLASS_ID = "class_id";

        private Params() {
            throw new AssertionError();
        }
    }

    public static final class Route {

        public static final String API_AUTH_ROUTE = "/api/rescope/*";
        public static final String API_INTERNAL_BANNER = "/api/internal/banner";
        public static final String API_INTERNAL_METRICS = "/api/internal/metrics";
        private static final String API_BASE_ROUTE = "/api/rescope/:version/";
        public static final String API_RESCOPE_FETCH = API_BASE_ROUTE + "scope/skipped";
        public static final String API_RESCOPE_CALCULATE = API_BASE_ROUTE + "scope/skipped";

        private Route() {
            throw new AssertionError();
        }
    }

    public static final class Misc {

        public static final String COMPETENCY_PLACEHOLDER = new UUID(0, 0).toString();
        public static final String USER_PLACEHOLDER = new UUID(0, 0).toString();

        private Misc() {
            throw new AssertionError();
        }
    }

}
