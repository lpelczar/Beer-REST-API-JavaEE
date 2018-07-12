package com.codecool.beerlovers.beerdb.util;

import java.util.HashMap;
import java.util.function.Function;

public interface URLQueryDecoder extends Function<String, HashMap<String, String>> {
    @Override
    HashMap<String, String> apply(String s);
}
