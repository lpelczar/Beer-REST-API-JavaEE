package com.codecool.beerlovers.beerdb.servlet;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
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

    @Test
    void post_brewery_return_status_code_201() {

        String jsonBrewery = "{ \"name\" : \"(512) New Company\", \"address1\" : \"407 Radam, F200\", " +
                "\"city\" : \"Austin\", \"state\" : \"Texas\", \"code\" : \"78745\", \"country\" : \"United States\", " +
                "\"phone\" : \"512.707.2337\", \"website\" : \"http://512brewing.com/\", \"descript\" : \"(512) Brewing"+
                " Company is a microbrewery located in the heart of Austin that brews for the community using as many " +
                "local, domestic and organic ingredients as possible.\" }";

        given().
                body(jsonBrewery).
        when().
                post("/breweries/").
        then().
                statusCode(201);
    }

    @Test
    void post_invalid_brewery_return_status_code_204() {

        String jsonBrewery = "{\"name\" : \"(512) New Company\", \"address1\" : \"407 Radam, F200\", " +
                "\"city\" : \"Austin\", \"state\" : \"Texas\", \"code\" : \"78745\", \"country\" : \"United States\", " +
                "\"phone\" : \"512.707.2337\", \"website\" : \"http://512brewing.com/\", \"descript\" : \"(512) Brewing"+
                " Company is a microbrewery located in the heart of Austin that brews for the community using as many " +
                "local, domestic and organic ingredients as possible.\" dsda}";

        given().
                body(jsonBrewery).
        when().
                post("/breweries/").
        then().
                statusCode(204);
    }

    @Test
    void post_brewery_with_id_return_status_code_204() {

        String jsonBrewery = "{\"id\" : 1, \"name\" : \"(512) New Company\", \"address1\" : \"407 Radam, F200\", " +
                "\"city\" : \"Austin\", \"state\" : \"Texas\", \"code\" : \"78745\", \"country\" : \"United States\", " +
                "\"phone\" : \"512.707.2337\", \"website\" : \"http://512brewing.com/\", \"descript\" : \"(512) Brewing"+
                " Company is a microbrewery located in the heart of Austin that brews for the community using as many " +
                "local, domestic and organic ingredients as possible.\" }";

        given().
                body(jsonBrewery).
        when().
                post("/breweries/").
        then().
                statusCode(204);
    }

    @Test
    void post_with_invalid_path_return_status_code_204() {
        when().
                post("/breweries/323").
        then().
                statusCode(204);
    }

    @Test
    void put_brewery_return_status_code_201() {

        String jsonBrewery = "{\"id\" : 1, \"name\" : \"Nowe\", \"address1\" : \"407 Radam, F200\", " +
                "\"city\" : \"Austin\", \"state\" : \"Texas\", \"code\" : \"78745\", \"country\" : \"United States\", " +
                "\"phone\" : \"512.707.2337\", \"website\" : \"http://512brewing.com/\", \"descript\" : \"(512) Brewing"+
                " Company is a microbrewery located in the heart of Austin that brews for the community using as many " +
                "local, domestic and organic ingredients as possible.\" }";

        given().
                body(jsonBrewery).
        when().
                put("/breweries/{id}", 1).
        then().
                statusCode(201);
    }

    @Test
    void put_brewery_invalid_path_return_status_code_204() {
        when().
                put("/breweries/").
        then().
                statusCode(204);

        when().
                put("/breweries").
                then().
                statusCode(204);
    }
}