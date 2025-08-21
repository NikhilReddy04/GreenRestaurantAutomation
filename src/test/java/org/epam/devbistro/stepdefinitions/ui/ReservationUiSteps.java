package org.epam.devbistro.stepdefinitions.ui;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.epam.devbistro.context.UiTestContext;
import org.epam.devbistro.pages.BookingPage;
import org.epam.devbistro.pages.ReservationPage;
import org.epam.devbistro.pages.SignInPage;
import org.epam.devbistro.utils.DriverManager;
import org.epam.devbistro.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReservationUiSteps {
    private WebDriver driver= DriverManager.getInstance().getDriver();
    BookingPage bookingPage=new BookingPage(driver);
    WaitUtils waitUtils=new WaitUtils(driver);
    SignInPage signInPage=new SignInPage(driver);
    ReservationPage reservationPage=new ReservationPage(driver);
    private final UiTestContext context;


    public ReservationUiSteps(UiTestContext context) {
        this.context = context;
    }


    @Given("The user booked a reservation and is in the reservations page")
    public void theUserBookedAReservationAndIsInTheReservationsPage() throws InterruptedException {
        BookingsUiSteps bookingsUiSteps=new BookingsUiSteps(context);
        bookingsUiSteps.theUserIsLoggedInAndIsOnTheTableBookingPage();
        bookingsUiSteps.theUserSelectsAnAvailableTimeSlot();
        bookingsUiSteps.theReservationShouldBeSuccessfullyMade();
        bookingPage.clickViewReservationsButton();

    }

    @When("user cancels the reservation")
    public void cancelsTheReservation() {
        driver.findElement(By.xpath("//button[.='Cancel']")).click();
        driver.findElement(By.xpath("//button[normalize-space()='Yes, cancel it']")).click();

    }

    @Then("The reservation cancelled message should be displayed")
    public void theReservationShouldBeCancelledInUi() {
        waitUtils.waitForElementToBeVisible(By.xpath("//div[text()='Reservation cancelled successfully!']"));
        Assert.assertTrue(driver.findElement(By.xpath("//div[text()='Reservation cancelled successfully!']")).isDisplayed());
    }

    @And("The status of reservation should be {string}")
    public void theStatusOfReservationShouldBe(String status) {
        driver.navigate().refresh();
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = tomorrow.format(formatter);
        Assert.assertTrue(bookingPage.getReservedSlotBySlotNumberAndDateAndStatus(status,formattedDate, context.getBookedSlot()).isDisplayed());
    }

    @When("user edits the reservation")
    public void userEditsTheReservation() {
        driver.findElement(By.xpath("//button[normalize-space()='Edit']")).click();
        waitUtils.waitForElementToBeVisible(By.xpath("//div[text()='You can modify the time slot and number of guests']"));
        Assert.assertTrue(driver.findElement(By.xpath("//div[text()='You can modify the time slot and number of guests']")).isDisplayed());
        bookingPage.clickFindTable();
        bookingPage.clickAndReturnAvailableTimeSlot();
        waitUtils.waitForElementToBeVisible(By.xpath("//button[normalize-space()='Update Reservation']"));
        driver.findElement(By.xpath("//button[normalize-space()='Update Reservation']")).click();
    }

    @Then("The reservation updated message should be displayed")
    public void theReservationUpdatedMessageShouldBeDisplayed() {
        Assert.assertTrue(driver.findElement(By.xpath("//h2[text()='Reservation Updated!']")).isDisplayed());
        String confirmationMessage=driver.findElement(By.xpath("//p[starts-with(text(),'Your table')]")).getText();
        String bookedSlot=reservationPage.getSlotNumber(confirmationMessage);
        context.setBookedSlot(bookedSlot);
        bookingPage.clickViewReservationsButton();
    }

    @And("The reservation should be edited")
    public void theReservationShouldBeEdited() {
         LocalDate tomorrow = LocalDate.now().plusDays(1);
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
     String formattedDate = tomorrow.format(formatter);
         Assert.assertTrue(bookingPage.getReservedSlotBySlotNumberAndDateAndStatus("Reserved",formattedDate, context.getBookedSlot()).isDisplayed());
    }

}