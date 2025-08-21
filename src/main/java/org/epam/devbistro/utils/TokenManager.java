package org.epam.devbistro.utils;

public class TokenManager {
    private static String token;

    public static void setToken(String bearerToken) {
        token = bearerToken;
    }

    public static String getToken() {
        return token;
    }

    public static String getBearerHeader() {
        return "Bearer " + token;
    }
}
