package com.codecool.beerlovers.beerdb.util;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public interface JsonUtils {

    String getStringFromHttpServletRequest(HttpServletRequest request);

    boolean checkJsonCompatibility(String jsonStr, Class<?> valueType) throws IOException;
}
