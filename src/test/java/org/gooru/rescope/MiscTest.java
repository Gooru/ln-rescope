package org.gooru.rescope;

import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 17/5/18.
 */
public class MiscTest {

    public static void main(String[] args) {
        MiscTest miscTest = new MiscTest();
        miscTest.run();
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
}
