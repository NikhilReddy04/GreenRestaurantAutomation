package org.epam.devbistro.stepdefinitions.ui;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.epam.devbistro.pages.SignUpPage;
import org.epam.devbistro.utils.ConfigReader;
import org.epam.devbistro.utils.DriverManager;
import org.epam.devbistro.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.testng.Assert.assertTrue;

public class SignUpSteps {
    private WebDriver driver = DriverManager.getInstance().getDriver();
    private SignUpPage signUpPage = new SignUpPage(driver);
    private WaitUtils waitUtils = new WaitUtils(driver);
    @When("the user enters {string} as first name, {string} as last name, {string} as email, {string} as password, and {string} as confirm password")
    public void theUserEntersAsFirstNameAsLastNameAsEmailAsPasswordAndAsConfirmPassword(String firstName, String lastName, String email, String password, String confirmPassword) {
        signUpPage.enterFirstName(firstName);
        signUpPage.enterLastName(lastName);
        signUpPage.enterEmail(email);
        signUpPage.enterPassword(password);
        signUpPage.enterConfirmPassword(confirmPassword);
    }

    @And("the user clicks the Sign Up button")
    public void theUserClicksTheSignUpButton() {
        signUpPage.clickSignUp();
    }

    @Then("the user should see an error message {string}")
    public void theUserShouldSeeAnErrorMessage(String errorMessage) {
        By toastLocator = By.xpath("//div[contains(@class,'Toastify__toast--error')]");
        WebElement toastElement = waitUtils.waitForElementToBeVisible(toastLocator);
        String actualMessage = toastElement.getText().trim();
        assertTrue(actualMessage.contains(errorMessage), "Expected toast message not shown. Found: " + actualMessage);
    }

    @Then("a temporary notification should appear with message {string} on existing page")
    public void aTemporaryNotificationShouldAppearWithMessageOnExistingPage(String expectedMessage) {
        By toastLocator = By.xpath("//div[contains(@class,'Toastify__toast--error')]");
        WebElement toastElement = waitUtils.waitForElementToBeVisible(toastLocator);
        String actualMessage = toastElement.getText();
        assertTrue(actualMessage.contains(expectedMessage), "Expected message not found in toast");
    }

    @Given("the user is on the signup page")
    public void theUserIsOnTheSignupPage() {
        String baseUrl = ConfigReader.get("BASE_URL").replaceAll("/$", "");
        driver.get(baseUrl + "/register");

    }
}
