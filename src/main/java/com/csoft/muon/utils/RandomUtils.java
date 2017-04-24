package com.csoft.muon.utils;

import java.util.Random;

import com.csoft.muon.domain.Item;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class RandomUtils {

    public static JsonObject getRandomBody(int index) {
        JsonObject item = new JsonObject();
        item.addProperty("id", index);
        item.addProperty("label", "item" + index);
        JsonArray array = new JsonArray();
        for (int i = 0; i < 5; i++) {
            array.add((new Random()).nextInt());
        }
        item.add("array", array);
        return item;
    }
    
    public static Item getRandomItem(int index) {
        String label = "label-" + index;
        return new Item(index, label);
    }

}
