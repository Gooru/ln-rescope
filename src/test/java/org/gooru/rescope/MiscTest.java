package org.gooru.rescope;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.gooru.rescope.infra.data.RescopeQueueModel;
import org.gooru.rescope.infra.data.RescopeSourceType;
import org.gooru.rescope.infra.services.itemfinder.SkippedItemsResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 17/5/18.
 */
public class MiscTest {

    public static void main(String[] args) {
        MiscTest miscTest = new MiscTest();
        miscTest.run1();
    }

    private void run1() {
        SkippedItemsResponse items = new SkippedItemsResponse();
        items.setCollectionsExternal(new ArrayList<>());
        items.setAssessmentsExternal(new ArrayList<>());
        items.setAssessments(getDummies());
        items.setCollections(getDummies());
        items.setLessons(getDummies());
        items.setUnits(getDummies());

        ObjectMapper mapper = new ObjectMapper();
        try {
            String value = mapper.writeValueAsString(items);
            System.out.println(value);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private List<String> getDummies() {
        return Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString());
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
