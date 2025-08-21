package org.epam.devbistro.stepdefinitions.api;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.epam.devbistro.context.ApiTestContext;
import org.epam.devbistro.mainContext.MainContext;
import org.epam.devbistro.model.SignInPayload;
import org.epam.devbistro.utils.TokenManager;
import org.testng.Assert;

import static org.testng.AssertJUnit.assertEquals;

public class SignInApiSteps {

    private final ApiTestContext context;
    private final MainContext mainContext;

    public SignInApiSteps(ApiTestContext context, MainContext mainContext) {
        this.context = context;
        this.mainContext=mainContext;
    }

    @Given("User has a valid signIn payload")
    public void userHasAValidSignInPayload() {
        SignInPayload payload = new SignInPayload("nikhil@gmail.com", "Nikhil@123");
        RequestSpecification request = RestAssured.given()
                .contentType("application/json")
                .body(payload);
        context.setRequest(request);
    }

    @Given("User has an invalid email signIn payload")
    public void userHasAnInvalidEmailPayload() {
        SignInPayload payload = new SignInPayload("Nikh@@@123.com", "Nikhred@123");
        context.setRequest(RestAssured.given().contentType("application/json").body(payload));
    }



    @Then("The response should contain a token")
    public void theResponseShouldContainAToken() {
        String token = context.getResponse().jsonPath().getString("accessToken");
        Assert.assertNotNull(token, "Token should not be null");
        context.setBearerToken(token);
        TokenManager.setToken(token);
    }

    @Then("The response should contain an error message {string}")
    public void theResponseShouldContainAnErrorMessage(String expectedErrorMessage) {
        String actualErrorMessage = context.getResponse().jsonPath().getString("message");
        Assert.assertEquals(actualErrorMessage, expectedErrorMessage, "Error message should match");
    }

    @Then("The response should contain a message {string}")
    public void theResponseShouldContainAMessage(String expectedMessage) {
        String actualMessage = context.getResponse().jsonPath().getString("message");
        Assert.assertEquals(actualMessage, expectedMessage, "Message should match");

    }



    @Given("User has a signIn payload with {string} and {string}")
    public void userHasASignInPayloadWithAnd(String username, String password) {
        SignInPayload payload = new SignInPayload(username, password);
        RequestSpecification request = RestAssured.given()
                .contentType("application/json")
                .body(payload);
        context.setRequest(request);
    }
}
