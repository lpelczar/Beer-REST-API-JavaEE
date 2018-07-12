package com.codecool.beerlovers.beerdb.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

@Component
public class JsonUtilsImpl implements JsonUtils {

    public String getStringFromHttpServletRequest(HttpServletRequest request) {
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

    @Override
    public boolean checkJsonCompatibility(String jsonStr, Class<?> valueType) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.readValue(jsonStr, valueType);
            return true;
        } catch (IOException e) {
            return false;
        }

    }
}
