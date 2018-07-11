package com.codecool.beerlovers.beerdb.util;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Function;

@Component
public interface HttpRequestToJsonString extends Function<HttpServletRequest, String> {
    @Override
    String apply(HttpServletRequest request);
}
