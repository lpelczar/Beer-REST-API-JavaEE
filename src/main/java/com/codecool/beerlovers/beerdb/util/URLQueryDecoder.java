package com.codecool.beerlovers.beerdb.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class URLQueryDecoder implements Function<String, HashMap<String, String>> {

    @Override
    public HashMap<String, String> apply(String s) {
        HashMap<String, String> result = new HashMap<>();
        List<String> keyValuePairs = Arrays.asList(s.split("&"));

        keyValuePairs.forEach(s1 -> {
            String[] split = s1.split("=");
            result.put(split[0], split[1]);
        });
        return result;
    }
}
