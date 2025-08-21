package org.epam.devbistro.stepdefinitions.api;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.epam.devbistro.context.ApiTestContext;
import org.testng.Assert;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.net.URL;


import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertNotNull;

public class DishesSteps {

    public Logger logger;
    private final ApiTestContext context;

    public DishesSteps(ApiTestContext context) {
        this.context = context;
        logger= LogManager.getLogger(this.getClass());

    }

    @Given("The user wants to fetch popular dishes")
    public void userWantsToFetchPopularDishes(){
        RequestSpecification request = RestAssured.given()
                .contentType("application/json");
        context.setRequest(request);
    }

    @Given("The user wants to fetch dishes of type {string} and sorted by {string}")
    public void userWantsToFetchDishesOfTypeAndSortedBy(String type,String sortBy){
        RequestSpecification request = RestAssured.given()
                .contentType("application/json")
                .queryParam("dishType",type)
                .queryParam("sort",sortBy);
        context.setRequest(request);
    }

    @Given("The user wants to fetch a particular dish")
    public void userWantsToFetchAParticularDish(){
        RequestSpecification request = RestAssured.given()
                .contentType("application/json");
        context.setRequest(request);
    }


    @And("The response should contain a list of dishes")
    public void validateTheResponseHasListOfDishes() throws MalformedURLException {
        Response response=context.getResponse();
        List<Map<String, Object>> dishes = response.jsonPath().getList("$");
        for (Map<String, Object> dish : dishes) {
            assertNotNull(dish.get("name"));
            assertNotNull(dish.get("price"));
            assertTrue(dish.get("weight").toString().endsWith("g"));
            new URL(dish.get("imageUrl").toString()); // Will throw MalformedURLException if invalid
        }

    }

    @And("The response should contain a list of dishes sorted by {string}")
    public void theResponseShouldContainAListOfDishesSortedBy(String sortingOrder) {
        Response response=context.getResponse();
        // Split the sorting string, e.g., "price,asc"
        String[] parts = sortingOrder.split(",");
        String field = parts[0].trim();
        String direction = parts[1].trim().toLowerCase();

        List<Map<String, Object>> dishes = response.jsonPath().getList("content");
        Assert.assertNotNull(dishes, "Content list is missing in the response");
        Assert.assertFalse(dishes.isEmpty(), "Content list is empty");


        for (Map<String, Object> dish : dishes) {
            Assert.assertTrue(dish.containsKey("price"), "Missing field: price");
            Assert.assertTrue(dish.containsKey("name"), "Missing field: name");
            Assert.assertTrue(dish.containsKey("weight"), "Missing field: weight");
            Assert.assertTrue(dish.containsKey("id"), "Missing field: id");
            Assert.assertTrue(dish.containsKey("state"), "Missing field: state");
            Assert.assertTrue(dish.containsKey("previewImageUrl"), "Missing field: previewImageUrl");
        }

        List<? extends Comparable> actualList;

        switch (field) {
            case "price":
                actualList = response.jsonPath().getList("content.price", Float.class);
                break;
            case "name":
            case "weight":
            case "id":
            case "state":
            case "previewImageUrl":
                actualList = response.jsonPath().getList("content." + field, String.class);
                break;
            default:
                throw new IllegalArgumentException("Unsupported field for sorting: " + field);
        }


        List<? extends Comparable> expectedList = new ArrayList<>(actualList);
        expectedList.sort(direction.equals("asc") ? Comparator.naturalOrder() : Comparator.reverseOrder());

        Assert.assertEquals(actualList, expectedList, "The list is not sorted by " + field + " in " + direction + " order.");
        }

    @And("The response should contain dish details")
    public void theResponseShouldContainDishDetails() {
        Response response=context.getResponse();
        JsonPath jsonPath = response.jsonPath();
        Map<String, Object> dish = jsonPath.getMap(""); // Extract root-level object


        String[] requiredFields = {
                "carbohydrates", "weight", "description", "calories", "vitamins", "fats",
                "price", "proteins", "dishType", "imageUrl", "name", "id", "state"
        };

        for (String field : requiredFields) {
            Assert.assertTrue(dish.containsKey(field), "Missing field: " + field);
            Object value = dish.get(field);
            Assert.assertNotNull(value, field + " should not be null");
            Assert.assertFalse(value.toString().trim().isEmpty(), field + " should not be empty");
        }


        Assert.assertTrue(dish.get("price").toString().matches("\\d+(\\.\\d+)?"), "Invalid price format");
        Assert.assertTrue(dish.get("calories").toString().matches("\\d+"), "Calories should be numeric");
        Assert.assertTrue(dish.get("imageUrl").toString().startsWith("http"), "Image URL should start with http/https");
    }

    @And("The response should contain a content message {string}")
    public void theResponseShouldContainAContentMessage(String expectedMessage) {
        String actualMessage = context.getResponse().jsonPath().getString("content");
        Assert.assertEquals(actualMessage, expectedMessage, "Message should match");
    }
}
