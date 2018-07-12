package com.codecool.beerlovers.beerdb.util;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

@Component
public class URLQueryDecoderImpl implements URLQueryDecoder {

    Logger log = Logger.getLogger(getClass().getName());

    @Override
    public HashMap<String, String> apply(String s) {
        HashMap<String, String> result = new HashMap<>();

        try {
            List<String> keyValuePairs = Arrays.asList(s.split("&"));

            keyValuePairs.forEach(s1 -> {
                String[] split = s1.split("=");
                result.put(split[0], split[1]);
            });
        } catch (Exception e) {
            log.warning("Query is invalid");
        }

        return result;
    }
}
