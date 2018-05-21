package org.gooru.rescope;

import java.util.UUID;

import org.gooru.rescope.infra.data.RescopeQueueModel;
import org.gooru.rescope.infra.data.RescopeSourceType;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 17/5/18.
 */
public class MiscTest {

    public static void main(String[] args) {
        MiscTest miscTest = new MiscTest();
        miscTest.run();
    }

    private void run1() {
      /*  RescopeQueueModel model = createNewRescopeQueueModel();
        String s1 = model.toJson();
        String s2 = model.toJsonUsingFallback();
        System.out.println(s1);
        System.out.println(s2);
        String m1, m2, m3, m4;
        m1 = RescopeQueueModel.fromJson(s1).toJson();
        m2 = RescopeQueueModel.fromJsonUsingFallback(s1).toJson();
        m3 = RescopeQueueModel.fromJson(s2).toJson();
        m4 = RescopeQueueModel.fromJsonUsingFallback(s2).toJson();
        System.out.println(m1);
        System.out.println(m2);
        System.out.println(m3);
        System.out.println(m4);
        m1 = RescopeQueueModel.fromJson(s1).toJsonUsingFallback();
        m2 = RescopeQueueModel.fromJsonUsingFallback(s1).toJsonUsingFallback();
        m3 = RescopeQueueModel.fromJson(s2).toJsonUsingFallback();
        m4 = RescopeQueueModel.fromJsonUsingFallback(s2).toJsonUsingFallback();
        System.out.println(m1);
        System.out.println(m2);
        System.out.println(m3);
        System.out.println(m4);
*/
    }

    private RescopeQueueModel createNewRescopeQueueModel() {
        RescopeQueueModel model;
        model = new RescopeQueueModel();
        model.setId(100L);
        model.setClassId(null);
        model.setCourseId(UUID.randomUUID());
        model.setUserId(UUID.randomUUID());
        model.setStatus(RescopeQueueModel.RQ_STATUS_DISPATCHED);
        model.setPriority(RescopeSourceType.ClassJoinByMembers.getOrder());
        return model;
    }

    private void run() {
        boolean result =  doTest();
        System.out.println(result);
    }

    private boolean doTest() {
        String setting = "{}";
        JsonObject jsonSetting = new JsonObject(setting);
        return Boolean.TRUE.equals(jsonSetting.getBoolean("rescope"));
    }

    public void initializeComponent(Vertx vertx, JsonObject config) {
        System.out.println("Context.isOnVertxThread : " + Context.isOnVertxThread());
        System.out.println("Computing with : " + Thread.currentThread());

        vertx.setTimer(1000, id -> {
            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            System.out.println("-----------SetTimerHandler");
            System.out.println("Computing with : " + Thread.currentThread());
            System.out.println("Context#isEventLoopContext : " + Vertx.currentContext().isEventLoopContext());
            System.out.println("Context#isWorkerContext : " + Vertx.currentContext().isWorkerContext());
            System.out.println(
                "Context#isMultiThreadedWorkerContext : " + Vertx.currentContext().isMultiThreadedWorkerContext());
            System.out.println("Context#isOnEventLoopThread : " + Context.isOnEventLoopThread());
            System.out.println("Context.isOnWorkerThread : " + Context.isOnWorkerThread());
            System.out.println("Context.isOnVertxThread : " + Context.isOnVertxThread());
            vertx.executeBlocking(future -> {
                System.out.println("-----------ExecuteBlockingHandler");
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
                System.out.println("-----------ExecuteBlockingCallback");
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
