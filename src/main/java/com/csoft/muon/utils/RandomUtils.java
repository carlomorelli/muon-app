package com.csoft.muon.utils;

import java.util.Random;
import java.util.stream.IntStream;

import com.csoft.muon.domain.Item;

public class RandomUtils {

    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    
    public static Item getRandomItem(int index) {
        String label = "label-" + index;
        return new Item(index, label);
    }
    
    public static String getRandomString(int length) {
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) {
            int index = rnd.nextInt() * CHARS.length();
            salt.append(CHARS.charAt(index));
        }
        return salt.toString();
    }

}
