package com.codecool.beerlovers.beerdb.servlet;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.when;
import static org.hamcrest.core.Is.is;

class BreweryServletTest {

    @Test
    void get_breweries_resource_returns_200_with_expected_id() {
        when().
                get("/breweries/{id}", 5).
                then().
                statusCode(200).
                body("id", is(5));
    }

    @Test
    void get_breweries_resource_returns_400_when_incorrect_path() {
        when().
                get("/breweries/test").
                then().
                statusCode(400);
    }

    @Test
    void get_breweries_resource_returns_404_when_not_found() {
        when().
                get("/breweries/{id}", 2000000000).
                then().
                statusCode(404);
    }

    @Test
    void get_breweries_resource_returns_status_code_200() {
        when().
                get("/breweries/").
                then().
                statusCode(200);
    }

}