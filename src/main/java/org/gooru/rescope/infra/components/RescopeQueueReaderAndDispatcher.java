package org.gooru.rescope.infra.components;

import static org.gooru.rescope.infra.constants.Constants.EventBus.MBEP_RESCOPE_QUEUE_PROCESSOR;

import org.gooru.rescope.infra.data.RescopeQueueModel;
import org.gooru.rescope.infra.services.RescopeQueueInitializerService;
import org.gooru.rescope.infra.services.RescopeQueueRecordDispatcherService;
import org.gooru.rescope.routes.utils.DeliveryOptionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * This is the timer based runner class which is responsible to read the Persisted queued requests and send them to
 * Event bus so that they can be processed by listeners. It does wait for reply, so that we do increase the backpressure
 * on TCP bus too much, however what is replied is does not matter as we do schedule another one shot timer to do the
 * similar stuff. For the first run, it re-initializes the status in the DB so that any tasks that were under processing
 * when the application shut down happened would be picked up again.
 *
 * @author ashish.
 */
public final class RescopeQueueReaderAndDispatcher implements Initializer, Finalizer {

    private static final RescopeQueueReaderAndDispatcher ourInstance = new RescopeQueueReaderAndDispatcher();
    private static final int delay = 1000;
    private static long timerId;
    private static boolean firstTrigger = false;
    private static final Logger LOGGER = LoggerFactory.getLogger(RescopeQueueReaderAndDispatcher.class);
    private Vertx vertx;
    private static final int RESCOPE_PROCESS_TIMEOUT = 300;

    public static RescopeQueueReaderAndDispatcher getInstance() {
        return ourInstance;
    }

    private RescopeQueueReaderAndDispatcher() {
    }

    @Override
    public void finalizeComponent() {
        vertx.cancelTimer(timerId);
    }

    @Override
    public void initializeComponent(Vertx vertx, JsonObject config) {
        this.vertx = vertx;

        timerId = vertx.setTimer(delay, new TimerHandler(vertx, firstTrigger));
    }

    static final class TimerHandler implements Handler<Long> {

        private final Vertx vertx;

        TimerHandler(Vertx vertx, boolean firstTrigger) {
            this.vertx = vertx;
        }

        @Override
        public void handle(Long event) {
            vertx.<RescopeQueueModel>executeBlocking(future -> {
                if (firstTrigger) {
                    LOGGER.debug("Timer handling for first trigger");
                    RescopeQueueInitializerService.build().initializeQueue();
                    firstTrigger = false;
                }
                LOGGER.debug("Timer handling to dispatch next record");
                RescopeQueueModel model = RescopeQueueRecordDispatcherService.build().getNextRecordToDispatch();
                future.complete(model);
            }, asyncResult -> {
                if (asyncResult.succeeded()) {
                    if (asyncResult.result().isModelPersisted()) {
                        vertx.eventBus().send(MBEP_RESCOPE_QUEUE_PROCESSOR, asyncResult.result().toJson(),
                            DeliveryOptionsBuilder.buildWithoutApiVersion(RESCOPE_PROCESS_TIMEOUT),
                            eventBusResponse -> {
                            timerId = vertx.setTimer(delay, new TimerHandler(vertx, firstTrigger));
                        });
                    } else {
                        timerId = vertx.setTimer(delay, new TimerHandler(vertx, firstTrigger));
                    }
                } else {
                    LOGGER.warn("Processing of record from queue failed. ", asyncResult.cause());
                    timerId = vertx.setTimer(delay, new TimerHandler(vertx, firstTrigger));
                }
            });

        }
    }

}
