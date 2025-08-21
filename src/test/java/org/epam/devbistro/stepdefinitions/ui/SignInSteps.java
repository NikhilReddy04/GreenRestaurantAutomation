package org.epam.devbistro.stepdefinitions.ui;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.epam.devbistro.pages.SignInPage;
import org.epam.devbistro.utils.ConfigReader;
import org.epam.devbistro.utils.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.epam.devbistro.utils.WaitUtils;


import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class SignInSteps {
    private WebDriver driver = DriverManager.getInstance().getDriver();
    SignInPage signInPage=new SignInPage(driver);
    WaitUtils waitUtils = new WaitUtils(driver);

    @Given("the user is on the login page")
    public void theUserIsOnTheLoginPage() {
        String baseUrl = ConfigReader.get("BASE_URL").replaceAll("/$", "");
        driver.get(baseUrl + "/login");

    }

    @When("the user enters valid username and password")
    public void theUserEntersValidUsernameAndPassword()
    {
        signInPage.enterEmail(ConfigReader.get("email"));
        signInPage.enterPassword(ConfigReader.get("password"));
    }

    @And("the user clicks on the login button")
    public void theUserClicksOnTheLoginButton() {
        signInPage.clickLogin();
    }

    @Then("the user should be redirected to the homepage")
    public void theUserShouldBeRedirectedToTheHomepage() {
        String homeTitle=signInPage.HomePageElement();
        assertEquals(homeTitle,"Green & Tasty");
    }



    @Then("a temporary notification should appear with message {string}")
    public void aTemporaryNotificationShouldAppearWithMessage(String expectedMessage) {
        By toastLocator = By.xpath("//div[contains(@class,'Toastify__toast--error')]");
        WebElement toastElement = waitUtils.waitForElementToBeVisible(toastLocator);
        String actualMessage = toastElement.getText();
        assertTrue(actualMessage.contains(expectedMessage), "Expected message not found in toast");
    }

    @When("the user enters invalid username and password")
    public void theUserEntersInvalidUsernameAndPassword() {
        signInPage.enterEmail(ConfigReader.get("invalid_email"));
        signInPage.enterPassword(ConfigReader.get("invalid_password"));
    }
}
