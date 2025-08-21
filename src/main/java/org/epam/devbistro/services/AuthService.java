package org.epam.devbistro.services;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.epam.devbistro.model.SignInPayload;
import org.epam.devbistro.utils.ApiConstants;

public class AuthService {

    public static String getAccessToken(String email, String password) {
        String baseUrl = ApiConstants.BASE_URI;
        Response response = RestAssured.given()
                .contentType("application/json")
                .body(new SignInPayload(email, password))
                .post(baseUrl + "/auth/sign-in");
        return response.jsonPath().getString("accessToken");
    }

    public static String getAccessToken() {
        return getAccessToken("nikhil@gmail.com", "Nikhil@123");
    }

    public static String getAccessTokenOfAnotherUser() {
        return getAccessToken("naruto@gmail.com", "Naruto@123");
    }

    public static String getAccessTokenOfWaiter() {
        return getAccessToken("waiter@gmail.com", "Raushan@123");
    }
}
