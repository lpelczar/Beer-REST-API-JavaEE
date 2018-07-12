package com.codecool.beerlovers.beerdb.servlet;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.core.Is.is;

public class CategoryServletTest {

    @Test
    void get_categories_resource_returns_200_with_expected_id() {
        when().
                get("/categories/{id}", 5).
                then().
                statusCode(200).
                body("id", is(5));
    }

    @Test
    void get_categories_resource_returns_400_when_incorrect_path() {
        when().
                get("/categories/test").
                then().
                statusCode(400);
    }

    @Test
    void get_categories_resource_returns_404_when_not_found() {
        when().
                get("/categories/{id}", 2000000000).
                then().
                statusCode(404);
    }

    @Test
    void get_categories_resource_returns_status_code_200() {
        when().
                get("/categories/").
                then().
                statusCode(200);
    }

    @Test
    void post_categories_return_201(){
        String json = "{\n" +
                "    \"name\": \"Other Category\"\n" +
                "}";
        given().
            body(json).
        when().
            post("/categories").
        then().
            statusCode(201);
    }

    @Test
    void test_invalid_post_categories_return_204(){
        String json = "{\n" +
                "    \"name\": \"Other Category\"\n" +
                "asdasd" +
                "}";
        given().
            body(json).
        when().
            post("/categories").
        then().
            statusCode(204);
    }

    @Test
    void test_post_categories_with_id_return_204(){
        String json = "{\n" +
                "    \"id\": 4,\n" +
                "    \"name\": \"German Ale\"\n" +
                "}";
        given().
            body(json).
        when().
            post("/categories").
        then().
            statusCode(204);
    }

    @Test
    void test_post_with_invalid_path_return_204(){
        when().
            post("/categories/291").
        then().
            statusCode(204);
    }

    @Test
    void test_put_valid_categories_return_201(){
        String json = "{\n" +
                "    \"id\": 4,\n" +
                "    \"name\": \"German Super Ale\"\n" +
                "}";
        given().
            body(json).
        when().
            put("/categories/4").
        then().
            statusCode(201);
    }

    @Test
    void put_category_invalid_path_return_status_code_204() {
        when().
                put("/categories/").
                then().
                statusCode(204);

        when().
                put("/categories/123/132").
                then().
                statusCode(204);
    }


}
