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
import org.epam.devbistro.utils.ConfigReader;
import org.epam.devbistro.utils.DriverManager;
import org.epam.devbistro.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class BookingsUiSteps {

    private final UiTestContext context;

    public BookingsUiSteps(UiTestContext context) {
        this.context = context;
    }

    private WebDriver driver= DriverManager.getInstance().getDriver();
    BookingPage bookingPage=new BookingPage(driver);
    WaitUtils waitUtils=new WaitUtils(driver);
    SignInPage signInPage=new SignInPage(driver);
    SignInSteps signInSteps=new SignInSteps();
    HomePage homePage=new HomePage(driver);
    ReservationPage reservationPage=new ReservationPage(driver);

    String baseUrl = ConfigReader.get("BASE_URL").replaceAll("/$", "");
    @Given("the user is on the table booking page")
    public void theUserIsOnTheTableBookingPage() {
        driver.get(baseUrl + "/book");
    }

    @Given("the user is logged in and is on the table booking page")
    public void theUserIsLoggedInAndIsOnTheTableBookingPage() {
        UiAuthService.logInTheUserInUi();
        waitUtils.waitForElementToBeVisible(By.xpath("//a[normalize-space()='Book a Table']"));
        homePage.clickBookTableButton();
        Assert.assertTrue(bookingPage.getBookATableText().isDisplayed());
    }
    @When("the user selects location {string}")
    public void theUserSelectsLocation(String visibleText) {
        bookingPage.selectLocationByText(visibleText);
    }



    @When("the user submits the booking form")
    public void theUserSubmitsTheBookingForm() {

        bookingPage.clickFindTable();
    }
    @Then("available tables should be displayed")
    public void availableTablesShouldBeDisplayed() {
        boolean isDisplayed = bookingPage.isTableResultsDisplayed();
        assertTrue(isDisplayed, "Available Tables header is not displayed!");
    }



    @When("the user books a table with location {string}, date {string}, time {string}, and guest count {int}")
    public void theUserBooksATableWithLocationDateTimeAndGuestCount(String location, String date, String time, int guests) throws InterruptedException {
        bookingPage.selectLocationByText(location);
        bookingPage.selectDate(date);
        bookingPage.selectTime(time);
        bookingPage.adjustAndValidateGuestCount(guests);

    }

    @When("The user selects time slot {string}")
    public void theUserSelectsTimeSlot(String timeSlot) {
        bookingPage.selectTimeSlotButton(timeSlot);

    }

    @Then("The reservation should be successfully made")
    public void theReservationShouldBeSuccessfullyMade() {

        waitUtils.waitForElementToBeVisible(By.xpath("//h2[contains(text(),'Reservation Confirmed!')]"));
        System.out.println(driver.findElement(By.xpath("//p[starts-with(text(),'Your table')]")).getText());
        String confirmationMessage=driver.findElement(By.xpath("//p[starts-with(text(),'Your table')]")).getText();
        String bookedSlot=reservationPage.getSlotNumber(confirmationMessage);
        context.setBookedSlot(bookedSlot);

    }

    @Given("The user selects table {string} and time slot {string}")
    public void theUserSelectsTableAndTimeSlot(String tableName, String timeSlot) {
        //  bookingPage.selectTable(tableName);
        bookingPage.selectTimeSlotButton(timeSlot);
    }

    @Then("The user makes a reservation")
    public void theUserMakesAReservation() {
        bookingPage.makeReservation(true); // Specify user is logged in
    }

    @Then("The user logs in and makes a reservation")
    public void theUserLogsInAndMakesAReservation() {
      //  bookingPage.selectTimeSlotButton("15:45 - 17:15");
        bookingPage.makeReservation(false); // Specify user needs to log in
    }


    @When("The user selects an available time slot and books it")
    public void theUserSelectsAnAvailableTimeSlot() throws InterruptedException {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = tomorrow.format(formatter);

        bookingPage.selectLocation("loc124");
        bookingPage.selectDate(formattedDate);
        bookingPage.selectTime("23:00");
        bookingPage.adjustAndValidateGuestCount(2);
        bookingPage.clickFindTable();
        Thread.sleep(3000);
        String availableTimeSlot=bookingPage.clickAndReturnAvailableTimeSlot();
        bookingPage.clickMakeReservationButton();
    }

    @And("The reservation should be displayed in reservations")
    public void theReservationShouldBeDisplayedInReservations() {
        bookingPage.clickViewReservationsButton();
        assertTrue(reservationPage.isHelloUserTextElementDisplayed());
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = tomorrow.format(formatter);
        assertTrue(bookingPage.getReservedSlotBySlotNumberAndDateAndStatus("Reserved",formattedDate,context.getBookedSlot()).isDisplayed());

    }

    @Given("the user wants to cancel all reservations in reservations page")
    public void theUserWantsToCancelAllReservationsInReservationsPage() {
        UiAuthService.logInTheUserInUi();
        driver.findElement(By.xpath("//a[normalize-space()='Reservation']")).click();
        List<WebElement> cancelButtons=driver.findElements(By.xpath("//button[.='Cancel']"));
        for(WebElement cancelButton:cancelButtons){
            cancelButton.click();
            driver.findElement(By.xpath("//button[normalize-space()='Yes, cancel it']")).click();

        }
    }
}
