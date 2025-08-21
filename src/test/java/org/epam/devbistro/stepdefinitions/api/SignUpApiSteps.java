package org.epam.devbistro.stepdefinitions.api;

import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import org.epam.devbistro.context.ApiTestContext;;
import org.epam.devbistro.model.SignUpPayload;

public class SignUpApiSteps {

    private final ApiTestContext context;

    public SignUpApiSteps(ApiTestContext context) {
        this.context = context;
    }
    @Given("User has a valid signUp payload")
    public void userHasAValidSignUpPayload() {
        int randomNum = (int)(Math.random() * 90000) + 10000; // generates a random 5-digit number
        String email = "user" + randomNum + "@gmail.com";
        SignUpPayload payload = new SignUpPayload("John", "Doe", email, "John@1234");
        context.setRequest(RestAssured.given().contentType("application/json").body(payload));
    }


    @Given("User has a signUp payload with existing email")
    public void userHasASignUpPayloadWithExistingEmail() {

        SignUpPayload payload = new SignUpPayload("Jane", "Doe", "nikhil@gmail.com", "Jane@1234");
        context.setRequest(RestAssured.given().contentType("application/json").body(payload));
    }

    @Given("User has a signup payload with empty firstname")
    public void userHasASignUpPayloadWithEmptyFirstname() {
        SignUpPayload payload = new SignUpPayload("", "Smith", "smith@example.com", "Smith@1234");
        context.setRequest(RestAssured.given().contentType("application/json").body(payload));
    }

    @Given("User has a signup payload with empty lastname")
    public void userHasASignUpPayloadWithEmptyLastname() {
        SignUpPayload payload = new SignUpPayload("Emily", "", "emily@example.com", "Emily@1234");
        context.setRequest(RestAssured.given().contentType("application/json").body(payload));
    }

    @Given("User has a signup payload with invalid password {string}")
    public void userHasASignUpPayloadWithInvalidPassword(String invalidPassword) {
        SignUpPayload payload = new SignUpPayload("Alan", "Turing", "alan@example.com", invalidPassword);
        context.setRequest(RestAssured.given().contentType("application/json").body(payload));
    }

    @Given("User has a signup payload with invalid email")
    public void userHasASignUpPayloadWithInvalidEmail() {
        SignUpPayload payload = new SignUpPayload("Sara", "Connor", "sara@@example", "Sara@1234");
        context.setRequest(RestAssured.given().contentType("application/json").body(payload));
    }
}
