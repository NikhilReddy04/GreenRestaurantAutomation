package org.epam.devbistro.stepdefinitions.api;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.epam.devbistro.context.ApiTestContext;
import org.testng.Assert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

public class LocationSteps {
    private static final Logger logger = LogManager.getLogger(LocationSteps.class);
    private final ApiTestContext context;

    public LocationSteps(ApiTestContext context) {
        this.context = context;
    }

    @Given("The user wants to fetch locations")
    public void userWantsToFetchLocations(){
        logger.info("Preparing request to fetch locations");
        RequestSpecification request = RestAssured.given()
                .contentType("application/json");
        context.setRequest(request);
    }

    @And("The response should contain a list of locations")
    public void validateTheListOfLocations() throws MalformedURLException {
        Response response = context.getResponse();
        logger.info("Validating response for list of locations");
        List<Map<String, Object>> locations = response.jsonPath().getList("$");

        assertNotNull(locations);
        assertFalse(locations.isEmpty(), "Response list is empty");

        for (Map<String, Object> location : locations) {
            logger.debug("Validating location: {}", location);

            assertNotNull(location.get("id"), "Location ID should not be null");
            assertTrue(location.get("id").toString().startsWith("loc"), "ID should start with 'loc'");

            assertNotNull(location.get("address"), "Address should not be null");
            assertFalse(location.get("address").toString().isBlank(), "Address should not be blank");

            String imageUrl = location.get("imageUrl").toString();
            assertNotNull(imageUrl, "Image URL should not be null");
            assertTrue(imageUrl.startsWith("http"), "Image URL must start with http or https");

            new URL(imageUrl);

            double rating = Double.parseDouble(location.get("rating").toString());
            assertTrue(rating >= 0 && rating <= 5, "Rating must be between 0 and 5");

            assertNotNull(location.get("description"), "Description should not be null");

            double capacity = Double.parseDouble(location.get("totalCapacity").toString());
            assertTrue(capacity > 0, "Total capacity must be greater than 0");

            assertTrue(location.containsKey("averageOccupancy"), "Missing 'averageOccupancy' field");
            assertNotNull(location.get("averageOccupancy"), "'averageOccupancy' should not be null");
        }
    }

    @Given("The user wants to fetch speciality dishes of a location")
    public void userWantsToFetchSpecialityDishesOfLocation(){
        logger.info("Preparing request to fetch speciality dishes of a location");
        RequestSpecification request = RestAssured.given()
                .contentType("application/json");
        context.setRequest(request);
    }

    @Given("The user wants to fetch {string} feedbacks of a location")
    public void userWantsToFetchFeedbacksOfALocation(String type){
        logger.info("Preparing request to fetch '{}' feedbacks of a location", type);
        RequestSpecification request=RestAssured.given()
                .contentType("application/json")
                .queryParam("type",type);
        context.setRequest(request);
    }

    @Given("The user wants to fetch locations select options")
    public void userWantsToFetchLocationsSelectOptions(){
        logger.info("Preparing request to fetch location select options");
        RequestSpecification request = RestAssured.given()
                .contentType("application/json");
        context.setRequest(request);
    }

    @And("The response should contain a list of speciality dishes")
    public void validateTheResponseContainingListOfSpecialityDishes() throws MalformedURLException {
        Response response = context.getResponse();
        logger.info("Validating response for list of speciality dishes");
        List<Map<String, Object>> dishes = response.jsonPath().getList("$");

        for (Map<String, Object> dish : dishes) {
            logger.debug("Validating dish: {}", dish);

            assertTrue(dish.containsKey("price"), "Missing 'price' field");
            double price = Double.parseDouble(dish.get("price").toString());
            assertTrue(price > 0, "Price must be a positive number");

            assertTrue(dish.containsKey("imageUrl"), "Missing 'imageUrl' field");
            String imageUrl = dish.get("imageUrl").toString();
            assertNotNull(imageUrl, "Image URL should not be null");
            assertTrue(imageUrl.startsWith("http"), "Image URL must start with http or https");
            new URL(imageUrl);

            assertTrue(dish.containsKey("name"), "Missing 'name' field");
            String name = dish.get("name").toString();
            assertFalse(name.trim().isEmpty(), "Dish name should not be empty");

            assertTrue(dish.containsKey("weight"), "Missing 'weight' field");
            String weight = dish.get("weight").toString();
            assertTrue(weight.matches("\\d+(g|kg)"), "Weight must be in format like '300g' or '1kg'");
        }
    }

    @And("The response should contain a list of feedbacks")
    public void validateTheResponseShouldContainListOfFeedbacks(){
        Response response = context.getResponse();
        logger.info("Validating response for list of feedbacks");

        Map<String, Object> pageable = response.jsonPath().getMap("pageable");
        List<Map<String, Object>> content = response.jsonPath().getList("content");

        logger.debug("Pageable info: {}", pageable);
        logger.debug("Feedback content: {}", content);

        assertThat("Pageable should not be null", pageable, is(notNullValue()));
        assertThat("Content list should not be null", content, is(notNullValue()));

        if(!content.isEmpty()){
            Map<String, Object> firstItem = content.get(0);
            logger.debug("First feedback item: {}", firstItem);
            assertThat(firstItem, hasKey("rate"));
            assertThat(firstItem, hasKey("comment"));
            assertThat(firstItem, hasKey("userName"));
            assertThat(firstItem, hasKey("type"));
        }
    }

    @And("The response should contain a list of locations select options")
    public void validateResponseContainingListOfLocationsSelectOptions(){
        Response response = context.getResponse();
        logger.info("Validating response for location select options");

        List<Map<String, String>> locations = response.jsonPath().getList("");

        for (Map<String, String> location : locations) {
            logger.debug("Validating location select option: {}", location);
            String address = location.get("address");
            String id = location.get("id");

            Assert.assertNotNull(address);
            Assert.assertFalse(address.trim().isEmpty());

            Assert.assertNotNull(id);
            Assert.assertFalse(id.trim().isEmpty());
            Assert.assertTrue(id.startsWith("loc"));
        }
    }
}
