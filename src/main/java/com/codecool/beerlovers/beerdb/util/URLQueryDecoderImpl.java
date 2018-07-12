package com.codecool.beerlovers.beerdb.util;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Component
public class URLQueryDecoderImpl implements URLQueryDecoder {

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
