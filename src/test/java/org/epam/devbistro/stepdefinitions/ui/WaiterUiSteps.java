package org.epam.devbistro.stepdefinitions.ui;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.epam.devbistro.context.UiTestContext;
import org.epam.devbistro.pages.WaiterPage;
import org.epam.devbistro.services.ApiServices;
import org.epam.devbistro.services.UiAuthService;
import org.epam.devbistro.utils.DriverManager;
import org.epam.devbistro.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.testng.AssertJUnit.assertTrue;

public class WaiterUiSteps {
    private final UiTestContext context;

    private WebDriver driver= DriverManager.getInstance().getDriver();
    WaitUtils waitUtils=new WaitUtils(driver);

    public WaiterUiSteps(UiTestContext context) {
        this.context = context;
    }
    WaiterPage waiterPage=new WaiterPage(driver);

    @Given("The waiter is logged in and is in reservation page")
    public void theWaiterIsLoggedInAndIsInReservationPage(){
        UiAuthService.logInTheWaiterInUi();
        Assert.assertTrue(waiterPage.getHelloWaiterText().isDisplayed());
    }

    @When("the waiter creates a reservation for a customer")
    public void theWaiterCreatesAReservationForACustomer() {
        LocalDate futureDay = LocalDate.now().plusDays(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = futureDay.format(formatter);

        Map<String,String> availableSlotAndTableMap= ApiServices.getAvailableSlotAndDateForAGivenDateAndLocationAndGuestCount(formattedDate,"loc124","2");

        System.out.println(availableSlotAndTableMap);
        context.setAvailableSlot(availableSlotAndTableMap.get("slot"));
        context.setAvailableTable(availableSlotAndTableMap.get("table"));

        waiterPage.clickCreateReservationButton();
        waiterPage.chooseTypeCustomer();
        waiterPage.enterCustomerEmail("naruto@gmail.com");
        waiterPage.setGuestCount(3);
        waiterPage.setCreateReservationDate(formattedDate);
        waiterPage.setCreateReservationSlot(availableSlotAndTableMap.get("slot"));
        waiterPage.setCreateReservationTable(availableSlotAndTableMap.get("table"));
        waiterPage.clickMakeAReservationButton();
        Assert.assertTrue(waiterPage.isSuccessfulConfirmationMessageVisible());
    }

    @Then("The reservation confirmed message should be visible")
    public void theReservationConfirmedMessageShouldBeVisible() {
        waitUtils.waitForElementToBeVisible(By.xpath("//div[text()='Reservation created successfully']"));
        Assert.assertTrue(driver.findElement(By.xpath("//div[text()='Reservation created successfully']")).isDisplayed());
    }

    @And("The reservation should be available in the reservations")
    public void theReservationShouldBeAvailableInTheReservations() {
        LocalDate futureDay = LocalDate.now().plusDays(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = futureDay.format(formatter);



        waiterPage.setSearchDate(formattedDate);
        waiterPage.setSearchTime("09:00");
        waiterPage.setSearchTable(context.getAvailableTable());
        waiterPage.clickSearchButton();

        String xpath=waiterPage.getReservationXpathByCustomerNameAndDateAndTableAndTimeSlot(context.getAvailableTable(),"Naruto Reddy",formattedDate,context.getAvailableSlot());
        Assert.assertTrue(driver.findElement(By.xpath(xpath)).isDisplayed());

    }

    @Given("Waiter created a reservation for a customer and wants to cancel it")
    public void waiterCreatedAReservationForACustomerAndWantsToCancelIt() {
        UiAuthService.logInTheWaiterInUi();
        Assert.assertTrue(waiterPage.getHelloWaiterText().isDisplayed());
        this.theWaiterCreatesAReservationForACustomer();
    }

    @When("the waiter cancels the reservation")
    public void theWaiterCancelsTheReservation() {
        LocalDate futureDay = LocalDate.now().plusDays(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = futureDay.format(formatter);

        waiterPage.setSearchDate(formattedDate);
        waiterPage.setSearchTime("09:00");
        waiterPage.setSearchTable(context.getAvailableTable());
        waiterPage.clickSearchButton();

        waiterPage.clickCancelButtonBasedOnTimeSlotUserNameDateAndTableNumber(context.getAvailableTable(),"Naruto Reddy",formattedDate, context.getAvailableSlot());
        waiterPage.clickConfirmCancellationButton();

    }



    @Then("The reservation cancelled message should be visible")
    public void theReservationCancelledMessageShouldBeVisible() {

        Assert.assertTrue(waiterPage.isCancellationMessageVisible());
    }

    @And("The reservation should not be available in the reservations")
    public void theReservationShouldNotBeAvailableInTheReservations() {
        LocalDate futureDay = LocalDate.now().plusDays(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = futureDay.format(formatter);

        boolean disappeared=waiterPage.isCancelledReservationDisappearing(waiterPage.getReservationXpathByCustomerNameAndDateAndTableAndTimeSlot(context.getAvailableTable(),"Naruto Reddy",formattedDate,context.getAvailableSlot()));
        Assert.assertTrue(disappeared, "Element is still visible in the DOM.");
    }

    @Given("Waiter created a reservation for a customer and wants to edit it")
    public void waiterCreatedAReservationForACustomerAndWantsToEditIt() {
        UiAuthService.logInTheWaiterInUi();
        Assert.assertTrue(waiterPage.getHelloWaiterText().isDisplayed());
        this.theWaiterCreatesAReservationForACustomer();
    }

    @When("the waiter edits the reservation")
    public void theWaiterEditsTheReservation() {
        LocalDate futureDay = LocalDate.now().plusDays(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = futureDay.format(formatter);

        waiterPage.setSearchDate(formattedDate);
        waiterPage.setSearchTime("09:00");
        waiterPage.setSearchTable(context.getAvailableTable());
        waiterPage.clickSearchButton();

        waiterPage.clickEditButtonBasedOnTimeSlotUserNameDateAndTableNumber(context.getAvailableTable(),"Naruto Reddy",formattedDate, context.getAvailableSlot());

        LocalDate postponeDay = LocalDate.now().plusDays(5);
        String formattedPostponeDay = postponeDay.format(formatter);

        waiterPage.setCreateReservationDate(formattedPostponeDay);

        Map<String,String> availableSlotAndTableMap= ApiServices.getAvailableSlotAndDateForAGivenDateAndLocationAndGuestCount(formattedDate,"loc124","2");

        context.setAvailableSlot(availableSlotAndTableMap.get("slot"));
        context.setAvailableTable(availableSlotAndTableMap.get("table"));

        waiterPage.setCreateReservationSlot(availableSlotAndTableMap.get("slot"));
        waiterPage.setCreateReservationTable(availableSlotAndTableMap.get("table"));

        waiterPage.clickPostponeReservationButton();


    }

    @Then("The reservation postponed message should be visible")
    public void theReservationPostponedMessageShouldBeVisible() {
        Assert.assertTrue(waiterPage.isPostponeReservationSuccessMessageVisible());
    }

    @And("The reservation should be postponed")
    public void theReservationShouldBePostponed() {
        LocalDate postponedDay = LocalDate.now().plusDays(5);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedPostponedDay = postponedDay.format(formatter);

        waiterPage.setSearchDate(formattedPostponedDay);
        waiterPage.setSearchTime("09:00");
        waiterPage.setSearchTable(context.getAvailableTable());
        waiterPage.clickSearchButton();

        String xpath=waiterPage.getReservationXpathByCustomerNameAndDateAndTableAndTimeSlot(context.getAvailableTable(),"Naruto Reddy",formattedPostponedDay,context.getAvailableSlot());
        waitUtils.waitForElementToBeVisible(By.xpath(xpath));
        Assert.assertTrue(driver.findElement(By.xpath(xpath)).isDisplayed());
    }

    @When("The waiter attempts to create a reservation without filling all required details")
    public void theWaiterAttemptsToCreateAReservationWithoutFillingAllRequiredDetails() {
        waiterPage.clickCreateReservationButton();
        waiterPage.clickMakeAReservationButton();
    }

    @Then("The reservation should not be made")
    public void theReservationShouldNotBeMade() {
        boolean reservationError=waiterPage.isErrorMessageDisplayed();
        assertTrue(reservationError);

    }

    @When("The waiter enters an unregistered email for the customer while creating a reservation")
    public void theWaiterEntersAnUnregisteredEmailForTheCustomer() {

        LocalDate futureDay = LocalDate.now().plusDays(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = futureDay.format(formatter);

        Map<String,String> availableSlotAndTableMap= ApiServices.getAvailableSlotAndDateForAGivenDateAndLocationAndGuestCount(formattedDate,"loc124","2");
        waiterPage.clickCreateReservationButton();
        waiterPage.chooseTypeCustomer();
        waiterPage.enterCustomerEmail("invalid@gmail.com");
        waiterPage.setGuestCount(3);
        waiterPage.setCreateReservationDate(formattedDate);
        waiterPage.setCreateReservationSlot(availableSlotAndTableMap.get("slot"));
        waiterPage.setCreateReservationTable(availableSlotAndTableMap.get("table"));
        waiterPage.clickMakeAReservationButton();

    }


    @Then("A validation error for customer non-existence should be shown and the reservation should not be created")
    public void ErrorForCustomerNonExistenceShouldBeShown() {
        assertTrue(waiterPage.isNonExistCustomerMessageDisplayed());
    }


    @When("The waiter enters an invalid email for the customer while creating a reservation")
    public void theWaiterEntersAnInvalidEmailForTheCustomer() {
        LocalDate futureDay = LocalDate.now().plusDays(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = futureDay.format(formatter);

        Map<String,String> availableSlotAndTableMap= ApiServices.getAvailableSlotAndDateForAGivenDateAndLocationAndGuestCount(formattedDate,"loc124","2");
        waiterPage.clickCreateReservationButton();
        waiterPage.chooseTypeCustomer();
        waiterPage.enterCustomerEmail("invalid@gmail");
        waiterPage.setGuestCount(3);
        waiterPage.setCreateReservationDate(formattedDate);
        waiterPage.setCreateReservationSlot(availableSlotAndTableMap.get("slot"));
        waiterPage.setCreateReservationTable(availableSlotAndTableMap.get("table"));
        waiterPage.clickMakeAReservationButton();
    }

    @Then("A validation error for invalid email should be shown and the reservation should not be created")
    public void aValidationErrorForInvalidEmailShouldBeShown() {
        assertTrue(waiterPage.isInvalidErrorMessageDisplayed());
    }
}
