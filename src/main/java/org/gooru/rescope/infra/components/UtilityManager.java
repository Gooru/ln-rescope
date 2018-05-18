package org.gooru.rescope.infra.components;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish.
 * This is a manager class to initialize the utilities, Utilities initialized
 * may depend on the DB or application state. Thus their initialization sequence
 * may matter. It is advisable to keep the utility initialization for last.
 */
public final class UtilityManager implements Initializer, Finalizer {
    private static final UtilityManager ourInstance = new UtilityManager();

    public static UtilityManager getInstance() {
        return ourInstance;
    }

    private UtilityManager() {
    }

    @Override
    public void finalizeComponent() {

    }

    @Override
    public void initializeComponent(Vertx vertx, JsonObject config) {
        System.out.println("Context.isOnVertxThread : " + Context.isOnVertxThread());
        System.out.println("Computing with : " + Thread.currentThread());

        vertx.setTimer(1000, id -> {
            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            System.out.println("Computing with : " + Thread.currentThread());
            System.out.println("Context#isEventLoopContext : " + Vertx.currentContext().isEventLoopContext());
            System.out.println("Context#isWorkerContext : " + Vertx.currentContext().isWorkerContext());
            System.out.println(
                "Context#isMultiThreadedWorkerContext : " + Vertx.currentContext().isMultiThreadedWorkerContext());
            System.out.println("Context#isOnEventLoopThread : " + Context.isOnEventLoopThread());
            System.out.println("Context.isOnWorkerThread : " + Context.isOnWorkerThread());
            System.out.println("Context.isOnVertxThread : " + Context.isOnVertxThread());
            vertx.executeBlocking(future -> {
                System.out.println("============================");
                System.out.println("Computing with : " + Thread.currentThread());
                System.out.println("Context#isEventLoopContext : " + Vertx.currentContext().isEventLoopContext());
                System.out.println("Context#isWorkerContext : " + Vertx.currentContext().isWorkerContext());
                System.out.println(
                    "Context#isMultiThreadedWorkerContext : " + Vertx.currentContext().isMultiThreadedWorkerContext());
                System.out.println("Context#isOnEventLoopThread : " + Context.isOnEventLoopThread());
                System.out.println("Context.isOnWorkerThread : " + Context.isOnWorkerThread());
                System.out.println("Context.isOnVertxThread : " + Context.isOnVertxThread());
                future.complete();
            }, asyncResult -> {
                System.out.println("============================");
                System.out.println("Computing with : " + Thread.currentThread());
                System.out.println("Context#isEventLoopContext : " + Vertx.currentContext().isEventLoopContext());
                System.out.println("Context#isWorkerContext : " + Vertx.currentContext().isWorkerContext());
                System.out.println(
                    "Context#isMultiThreadedWorkerContext : " + Vertx.currentContext().isMultiThreadedWorkerContext());
                System.out.println("Context#isOnEventLoopThread : " + Context.isOnEventLoopThread());
                System.out.println("Context.isOnWorkerThread : " + Context.isOnWorkerThread());
                System.out.println("Context.isOnVertxThread : " + Context.isOnVertxThread());
            });
        });
    }
}
