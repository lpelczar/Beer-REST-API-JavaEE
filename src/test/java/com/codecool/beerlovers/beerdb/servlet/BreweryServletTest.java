package com.codecool.beerlovers.beerdb.servlet;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.when;
import static org.hamcrest.core.IsEqual.equalTo;

class BreweryServletTest {

    @Test
    void breweries_resource_returns_200_with_expected_id() {
        when().
                get("/breweries/{id}", 5).
                then().
                statusCode(200).
                body("id", equalTo(5));
    }
}