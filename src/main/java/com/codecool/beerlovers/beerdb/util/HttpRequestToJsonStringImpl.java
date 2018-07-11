package com.codecool.beerlovers.beerdb.util;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

@Component
public class HttpRequestToJsonStringImpl implements HttpRequestToJsonString {


    @Override
    public String apply(HttpServletRequest request) {
        String result = null;
        try {
            result = request.getReader()
                    .lines()
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
