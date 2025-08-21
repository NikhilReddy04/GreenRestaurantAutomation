package org.epam.devbistro.stepdefinitions.api;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.epam.devbistro.context.ApiTestContext;
import org.epam.devbistro.utils.ApiConstants;

import static org.testng.AssertJUnit.assertEquals;

public class CommonSteps {
    private final ApiTestContext context;

    public CommonSteps(ApiTestContext context) {
        this.context = context;
    }
    @Then("The response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatusCode) {
        assertEquals("Status code mismatch", expectedStatusCode, context.getResponse().getStatusCode());
        context.getResponse().then().statusCode(expectedStatusCode); // Optional second check
    }

    @When("User sends a POST request to {string}")
    public void userSendsAPostRequestTo(String endpoint) {
        String BASE_URL = ApiConstants.BASE_URI;
        Response response = context.getRequest().when().post(BASE_URL + endpoint);
        context.setResponse(response);
    }

    @Given("The user sends a GET request to {string}")
    public void userSendsAGetRequestTo(String endPoint){
        String BASE_URL = ApiConstants.BASE_URI;
        Response response = context.getRequest().when().get(BASE_URL + endPoint);
        context.setResponse(response);
    }


}
