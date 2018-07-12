package com.codecool.beerlovers.beerdb.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class URLQueryDecoderTest {


    private URLQueryDecoder urlQueryDecoder;

    @BeforeEach
    public void setup() {

        this.urlQueryDecoder = new URLQueryDecoderImpl();
    }

    @Test
    public void applyShouldReturnEmptyHashmapIfQueryIsInvalid() {

        HashMap<String, String> result = urlQueryDecoder.apply("asdasdadsd");

        assertTrue(result.isEmpty());

    }

    @Test
    public void applyShouldReturnEmptyHashmapIfQueryIsNull() {

        HashMap<String, String> result = urlQueryDecoder.apply(null);

        assertTrue(result.isEmpty());

    }

    @Test
    public void applyShouldReturnEmptyHashmapIfQueryIsEmpty() {

        HashMap<String, String> result = urlQueryDecoder.apply("");

        assertTrue(result.isEmpty());

    }

    @Test
    public void apply_ShouldReturnMapWithCorrectContent() {

        String query = "from=50&to=100";

        HashMap<String, String> applyResult = urlQueryDecoder.apply(query);

        assertEquals(2, applyResult.size());
        assertEquals("50", applyResult.get("from"));
        assertEquals("100", applyResult.get("to"));
    }
}