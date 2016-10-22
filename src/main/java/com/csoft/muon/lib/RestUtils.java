package com.csoft.muon.lib;

import java.util.Arrays;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class RestUtils {

    public static JsonObject getRandomBody(int index) {
        JsonObject json = new JsonObject()
                .put("id", index)
                .put("label", "item" + index)
                .put("array", new JsonArray(Arrays.asList(78, 47)));
        return json;
    }
}
