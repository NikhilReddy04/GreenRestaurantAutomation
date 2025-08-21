package org.epam.devbistro.hooks;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import org.epam.devbistro.context.ApiTestContext;

public class RestAssuredHooks {

    private final ApiTestContext context;

    // Constructor injection of shared context
    public RestAssuredHooks(ApiTestContext context) {
        this.context = context;
    }

    @Before
    public void setUpRequestSpecification() {
//        RequestSpecification request = RestAssured
//                .given()
//                .baseUri("https://api.example.com") // Replace with your base URL
//                .contentType("application/json");
//
//        context.setRequest(request);
    }

    @After
    public void tearDownScenario() {
        // Optional: Clear request/response after each scenario
//        context.setRequest(null);
//        context.setResponse(null);
    }
}
