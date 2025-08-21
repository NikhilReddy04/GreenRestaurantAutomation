package org.epam.devbistro.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class BookingPage extends BasePage {
    SignInPage signInPage;

    @FindBy(xpath = "//h3[normalize-space()='Book a Table']")
    private WebElement bookATableText;

    @FindBy(xpath = "//select[@name='locationId']")
    private WebElement dropdown;

    @FindBy(name = "date")
    private WebElement dateInput;

    @FindBy(name = "time")
    private WebElement timeInput;

    @FindBy(xpath = "//button[text()='-']")
    private WebElement decreaseGuestCountBtn;

    @FindBy(xpath = "//button[text()='+']")
    private WebElement increaseGuestCountBtn;

    @FindBy(xpath = "//span[@class='text-lg font-semibold']")
    private WebElement guestCountDisplay;

    @FindBy(xpath = "//button[text()='Find a Table']")
    private WebElement findTableBtn;

    @FindBy(xpath = "//h3[text()='Available Tables']")
    private WebElement availableTablesHeader;

    @FindBy(xpath = "//span[contains(@class,'truncate')]")
    private List<WebElement> availableTimeSlots;

    @FindBy(xpath = "//a[contains(.,'Reservation')]")
    private WebElement navigateToReservationsButton;

    @FindBy(xpath = "//button[normalize-space()='View Reservations']")
    private WebElement viewReservationsButton;

    public BookingPage(WebDriver driver) {
        super(driver);
        this.signInPage=new SignInPage(driver);
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public WebElement getBookATableText(){
        return this.bookATableText;
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public void selectLocation(String locationId) {
        waitUtils.waitForElementToBeVisible(By.xpath("//select[@name='locationId']"));
        waitUtils.waitForElementClickable(By.xpath("//select[@name='locationId']"));
        Select locationSelect = new Select(dropdown);

        locationSelect.selectByValue(locationId);
    }


    public void selectDate(String date)  {

        String script =
                "const input = document.querySelector(\"input[name='date']\");" +
                        "const nativeInputValueSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
                        "nativeInputValueSetter.call(input, '" + date + "');" +
                        "const ev2 = new Event('input', { bubbles: true}); input.dispatchEvent(ev2);" +
                        "const ev = new Event('change', { bubbles: true}); input.dispatchEvent(ev);";

        ((JavascriptExecutor) driver).executeScript(script);

    }

    public void selectTime(String time) {
        // Wait for the element to become visible
        waitUtils.waitForElementToBeVisible(By.name("time"));
        timeInput.clear();

        // Send the new time in "HH:mm" format
        timeInput.sendKeys(time);
        String selectedTime = timeInput.getAttribute("value");


        // Verify that the time was set correctly
        if (!selectedTime.equals(time)) {
            throw new IllegalStateException("Time was not set correctly. Expected: " + time + ", but got: " + selectedTime);
        }
    }

    public void increaseGuestCount() {
        increaseGuestCountBtn.click();
    }

    public void decreaseGuestCount() {
        decreaseGuestCountBtn.click();
    }

    public String getGuestCount() {
        return guestCountDisplay.getText();
    }

    public void clickFindTable() {
        findTableBtn.click();

    }

    public void adjustAndValidateGuestCount(int expectedCount) {
        // Get current guest count from the span element
        int currentCount = Integer.parseInt(guestCountDisplay.getText());
        // Decrease guest count if current is greater than expected
        while (currentCount > expectedCount) {
            decreaseGuestCountBtn.click();
            currentCount = Integer.parseInt(guestCountDisplay.getText());

        }

        // Increase guest count if current is less than expected
        while (currentCount < expectedCount) {
            increaseGuestCountBtn.click();
            currentCount = Integer.parseInt(guestCountDisplay.getText());
            //   System.out.println("Guest count increased to: " + currentCount);
        }

        // Validate the final guest count
        if (currentCount != expectedCount) {
            throw new AssertionError(
                    "Guest count mismatch! Expected: " + expectedCount + ", but got: " + currentCount);
        }
    }

    public boolean isTableResultsDisplayed() {
        return availableTablesHeader.isDisplayed(); // Returns true if the header is visible
    }


    public void selectLocationByText(String visibleText) {
        waitUtils.waitForTextToBePresentInElement(By.xpath("//select[@name='locationId']"), visibleText);
        Select locationSelect = new Select(dropdown);
        locationSelect.selectByVisibleText(visibleText);
    }

    public String getSelectedDate() {
        return dateInput.getAttribute("value"); // Retrieve the value to verify it matches
    }

    public String getSelectedTime() {
        // Get the current value of the time input field
        return timeInput.getAttribute("value");
    }
//---------------------------------------Booking table----------------------

    public void selectTimeSlotButton(String timeSlotText) {
        // Build XPath dynamically to locate the <button> based on the time slot text
        String timeSlotXPath = "(//span[@class='truncate'][normalize-space()='" + timeSlotText + "'])[1]/parent::button";

        try {
            // Wait for the button to be present using WebDriverWait
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement timeSlotButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(timeSlotXPath)));

            // Scroll to ensure the button is visible
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", timeSlotButton);

            // Click the button
            timeSlotButton.click();

            // Log success for debugging
            System.out.println("Clicked the time slot button: " + timeSlotText);
        } catch (TimeoutException e) {
            throw new NoSuchElementException("Time slot button with text '" + timeSlotText + "' was not found within the timeout period.");
        }
    }


    public void clickMakeReservationButton() {
        // Locate the "Make a Reservation" button using its XPath
        WebElement reservationButton = driver.findElement(By.xpath("//button[normalize-space()='Make a Reservation']"));

        // Ensure the button is visible and clickable
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", reservationButton);

        // Click the button
        reservationButton.click();

        // Log action for debugging
        System.out.println("Clicked the 'Make a Reservation' button.");
    }

    public void makeReservation(boolean isLoggedIn) {
        if (isLoggedIn) {
            clickMakeReservationButton();
        } else {
            // Handle login flow first
            WebElement loginButton = driver.findElement(By.xpath("//button[normalize-space()='Log in']"));
            loginButton.click();

            // Login credentials
            signInPage.loginAndRedirectToBookings();

            // After successful login, retry clicking "Make a Reservation"
            selectTimeSlotButton("15:45 - 17:15");
            clickMakeReservationButton();
            System.out.println("Make reservation");
        }
    }

    public String clickAndReturnAvailableTimeSlot() {
        for (WebElement slot : availableTimeSlots) {
            if (slot.isDisplayed()) {
                String timeSlotText = slot.getText();
                slot.click();
                return timeSlotText;
            }
        }
        throw new NoSuchElementException("No visible time slot found");
    }

    public void clickNavigateToReservationsButton(){
        navigateToReservationsButton.click();
    }

    public void clickViewReservationsButton(){
        viewReservationsButton.click();
    }


    public WebElement getReservedSlotBySlotNumberAndDateAndStatus(String status,String formattedDate, String bookedSlot) {
        String xpath = "//div//span[contains(text(),'"+status+"')]" +
                "//parent::div//following-sibling::div" +
                "//p[contains(text(),'" + formattedDate + "')]" +
                "//following-sibling::p[contains(text(),'" + bookedSlot + "')]";

        return driver.findElement(By.xpath(xpath));
    }
}





