package org.epam.devbistro.services;

import org.epam.devbistro.pages.SignInPage;
import org.epam.devbistro.utils.ConfigReader;
import org.epam.devbistro.utils.DriverManager;
import org.openqa.selenium.WebDriver;

import static org.testng.Assert.assertEquals;

public class UiAuthService {

    public static void logInWithCredentials(String email, String password) {
        WebDriver driver = DriverManager.getInstance().getDriver();
        SignInPage signInPage = new SignInPage(driver);
        String baseUrl = ConfigReader.get("BASE_URL").replaceAll("/$", "");

        driver.get(baseUrl + "/login");
        signInPage.enterEmail(email);
        signInPage.enterPassword(password);
        signInPage.clickLogin();

        String homeTitle = signInPage.HomePageElement();
        assertEquals(homeTitle, "Green & Tasty");
    }

    public static void logInTheUserInUi() {
        logInWithCredentials(ConfigReader.get("email"), ConfigReader.get("password"));
    }

    public static void logInTheWaiterInUi() {
        logInWithCredentials(ConfigReader.get("waiter_email"), ConfigReader.get("waiter_password"));
    }
}
