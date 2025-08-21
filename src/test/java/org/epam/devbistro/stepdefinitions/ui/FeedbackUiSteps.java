package org.epam.devbistro.stepdefinitions.ui;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.epam.devbistro.context.UiTestContext;
import org.epam.devbistro.pages.BookingPage;
import org.epam.devbistro.pages.HomePage;
import org.epam.devbistro.pages.ReservationPage;
import org.epam.devbistro.pages.SignInPage;
import org.epam.devbistro.services.UiAuthService;
import org.epam.devbistro.utils.DriverManager;
import org.epam.devbistro.utils.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class FeedbackUiSteps {
    private WebDriver driver= DriverManager.getInstance().getDriver();
    ReservationPage reservationPage=new ReservationPage(driver);
    private final UiTestContext context;

    HomePage homePage=new HomePage(driver);


    public FeedbackUiSteps(UiTestContext context) {
        this.context = context;
    }

    @Given("the user is logged in and is in reservations page")
    public void theUserIsLoggedInAndIsInReservationPage(){
        UiAuthService.logInTheUserInUi();
        homePage.clickReservationsButton();
        Assert.assertTrue(reservationPage.isHelloUserTextElementDisplayed());

    }

    @When("The user gives feedback to a reservation")
    public void theUserGivesFeedbackToAReservation() {
        reservationPage.clickUpdateFeedbackButton();
        Assert.assertTrue(reservationPage.isFeedbackDetailMessageDisplayed());
        reservationPage.clickEditFeedbackButton();
        Assert.assertTrue(reservationPage.isEditFeedbackTextMessageDisplayed());
        reservationPage.setServiceRatingTo(5);
        reservationPage.setServiceComment("Service by Naruto was exceptional");

        reservationPage.clickCulinaryExperience();

        reservationPage.setCuisineRatingTo(5);
        reservationPage.setCuisineComment("Ramen was amazing");

        reservationPage.clickInnerUpdateFeedbackButton();




    }

    @Then("the user should see feedback updated message")
    public void theUserShouldSeeFeedbackUpdatedMessage() {
        Assert.assertTrue(reservationPage.isSuccessfulFeedbackMessageVisible());
    }

    @And("The feedback should be visible in restaurant's feedbacks")
    public void theFeedbackShouldBeVisibleInRestaurantSFeedbacks() {
        reservationPage.clickMainPageButton();
        homePage.navigateToLocationWithId("loc123");
        homePage.isLocationNameVisible("locationName");

        homePage.isServiceFeedbackPresentWith("Service by Naruto was exceptional");
        homePage.clickCuisineExperienceTab();
        homePage.isCuisineFeedbackPresentWith("Ramen was amazing");
    }
}
