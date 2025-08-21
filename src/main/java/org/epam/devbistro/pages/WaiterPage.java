package org.epam.devbistro.pages;

import org.epam.devbistro.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaiterPage extends BasePage{
    private WebDriver driver;
    private WaitUtils waitUtils;

    @FindBy(xpath = "//p[starts-with(text(),'Hello')]")
    private WebElement helloWaiterText;

    @FindBy(xpath = "//input[@type='date']")
    private WebElement dateInput;

    @FindBy(xpath = "//input[@type='time']")
    private WebElement timeInput;

    @FindBy(xpath = "//select")
    private WebElement tableDropDown;

    @FindBy(xpath = "//button[normalize-space()='Search']")
    private WebElement searchButton;

    @FindBy(xpath = "//button[normalize-space()='Create Reservation']")
    private  WebElement createReservation;

    @FindBy(xpath = "//input[@name='clientType']/following-sibling::label[text()='Visitor']")
    private WebElement visitorRadioButton;

    @FindBy(xpath = "//input[@name='clientType']/following-sibling::label[text()='Existing Customer']")
    private WebElement customerRadioButton;

    @FindBy(xpath = "//label[text()=\"Customer's Email\"]//following-sibling::div//input")
    private WebElement customerEmailInputField;

    @FindBy(xpath = "//span[.='Guests']/parent::div/parent::div//button[1]")
    private WebElement decreaseGuestCountButton;

    @FindBy(xpath = "//span[.='Guests']/parent::div/parent::div//button[2]")
    private WebElement increaseGuestCountButton;

    @FindBy(xpath = "//label[text()='Date']/following-sibling::input[@type='date']")
    private WebElement createReservationDateInput;

    @FindBy(xpath = "//label[text()='Time Slot']/following-sibling::div//select")
    private WebElement createReservationSelectTimeSlotDropdown;

    @FindBy(xpath = "//label[text()='Table']/following-sibling::select")
    private WebElement createReservationSelectTableDropdown;

    @FindBy(xpath = "//button[normalize-space()='Make a Reservation']")
    private WebElement makeAReservationButton;

    @FindBy(xpath = "//button[normalize-space()='Postpone Reservation']")
    private WebElement postponeReservationButton;

    @FindBy(xpath = "//button[normalize-space()='Yes, Cancel']")
    private WebElement confirmCancellationButton;

    @FindBy(xpath = "//div[normalize-space()='Reservation created successfully']")
    private WebElement successfulReservationMessage;

    @FindBy(xpath = "//div[normalize-space()='Reservation cancelled successfully!']")
    private WebElement successfulCancellationMessage;

    @FindBy(xpath = "//div[normalize-space()='Reservation postponed successfully']")
    private WebElement successfulPostponeMessage;

    @FindBy(xpath = "//button[normalize-space()='Sign out']")
    private WebElement signOutButton;

    @FindBy(xpath = "//button[@id='profile-button']")
    private WebElement profileButton;

    @FindBy(xpath="//div[normalize-space(text())='Please fill in all required fields']")
    private WebElement reservationError;

    @FindBy(xpath="//div[normalize-space(text())='Please enter a valid email address']")
    private WebElement invalidEmailMessage;

    @FindBy(xpath="//div[@class='mb-4 p-3 bg-red-50 border border-red-200 text-red-600 rounded-lg text-sm']")
    private WebElement inavlidCustomer;

    public WaiterPage(WebDriver driver) {
        super(driver);
    }

    //Actions
    public WebElement getHelloWaiterText(){
        return helloWaiterText;
    }



    public boolean isSuccessfulConfirmationMessageVisible(){
        return successfulReservationMessage.isDisplayed();
    }

    public boolean isCancellationMessageVisible(){
        waitUtils.waitForElementToBeVisible(By.xpath("//div[normalize-space()='Reservation cancelled successfully!']"));
        return successfulCancellationMessage.isDisplayed();
    }


    public void setSearchDate(String date){
        String script =
                         "const input = arguments[0];" +
                        "const nativeInputValueSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
                        "nativeInputValueSetter.call(input, arguments[1]);" +
                        "input.dispatchEvent(new Event('input', { bubbles: true }));" +
                        "input.dispatchEvent(new Event('change', { bubbles: true }));";

        ((JavascriptExecutor) driver).executeScript(script, dateInput, date);
    }

    public void setSearchTime(String time) {


        // Send the new time in "HH:mm" format
        timeInput.sendKeys(time);
        String selectedTime = timeInput.getAttribute("value");


        // Verify that the time was set correctly
        if (!selectedTime.equals(time)) {
            throw new IllegalStateException("Time was not set correctly. Expected: " + time + ", but got: " + selectedTime);
        }
    }

    public void setSearchTable(String table) {
        Select selectTableDropdown=new Select(tableDropDown);
        selectTableDropdown.selectByValue(table);
    }

    public void clickSearchButton(){
        searchButton.click();
    }

    public void clickCreateReservationButton(){
        createReservation.click();
    }

    public void chooseTypeVisitor(){
        visitorRadioButton.click();
    }

    public void chooseTypeCustomer(){
        customerRadioButton.click();
    }

    public void enterCustomerEmail(String customerEmail){
        String xpath = "//label[text()=\"Customer's Email\"]//following-sibling::div//input";
        waitUtils.waitForElementToBeVisible(By.xpath(xpath));
        customerEmailInputField.sendKeys(customerEmail);
    }

    public void setGuestCount(int count){
        decreaseGuestCountButton.click();
        increaseGuestCountButton.click();
    }

    public void setCreateReservationDate(String date){
        String script =
                "const input = arguments[0];" +
                        "const nativeInputValueSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
                        "nativeInputValueSetter.call(input, arguments[1]);" +
                        "input.dispatchEvent(new Event('input', { bubbles: true }));" +
                        "input.dispatchEvent(new Event('change', { bubbles: true }));";

        ((JavascriptExecutor) driver).executeScript(script, createReservationDateInput, date);
    }

    public void setCreateReservationSlot(String slot){
        Select selectSlotDropSown=new Select(createReservationSelectTimeSlotDropdown);
        selectSlotDropSown.selectByVisibleText(slot);
    }

    public void setCreateReservationTable(String table){
        Select selectTableDropdown=new Select(createReservationSelectTableDropdown);
        selectTableDropdown.selectByValue(table);
    }

    public void clickMakeAReservationButton(){
        makeAReservationButton.click();
    }

    public String getReservationXpathByCustomerNameAndDateAndTableAndTimeSlot(String table,String customerName,String date,String timeSlot){
        String xpath = String.format(
                "//div[text()='%s']/../.." +
                        "//span[normalize-space()='Booked for: %s']" +
                        "/parent::p/following-sibling::p[normalize-space()='%s']" +
                        "/../p[text()='%s']",
                table, customerName, date, timeSlot
        );
        return xpath;
    }

    public void clickCancelButtonBasedOnTimeSlotUserNameDateAndTableNumber(String tableNumber,String userName, String date,String timeSlot) {
        String xpath = String.format(
                "//div[text()='%s']/../..//span[normalize-space()='Booked for: %s']/parent::p/following-sibling::p[normalize-space()='%s']/../p[text()='%s']/../../div//button[normalize-space()='Cancel']",
                tableNumber, userName, date, timeSlot
        );

        WebElement cancelButton = driver.findElement(By.xpath(xpath));
        cancelButton.click();
    }

    public void clickEditButtonBasedOnTimeSlotUserNameDateAndTableNumber(String tableNumber,String userName, String date,String timeSlot){
        String xpath = String.format(
                "//div[text()='%s']/../..//span[normalize-space()='Booked for: %s']/parent::p/following-sibling::p[normalize-space()='%s']/../p[text()='%s']/../../div//button[normalize-space()='Postpone']",
                tableNumber, userName, date, timeSlot
        );

        WebElement postponeButton = driver.findElement(By.xpath(xpath));
        postponeButton.click();
    }

    public void clickConfirmCancellationButton(){
        waitUtils.waitForElementToBeVisible(By.xpath("//div[normalize-space()='Reservation created successfully']"));
        confirmCancellationButton.click();
    }

    public void clickProfileButton(){
        waitUtils.waitForElementClickable(By.xpath("//button[@id='profile-button']"));
        profileButton.click();
    }
    public void clickLogoutButton(){
        signOutButton.click();
    }

    public boolean isCancelledReservationDisappearing(String xpath){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)));
    }

    public void clickPostponeReservationButton(){
        postponeReservationButton.click();
    }

    public boolean isPostponeReservationSuccessMessageVisible(){
        waitUtils.waitForElementToBeVisible(By.xpath("//div[normalize-space()='Reservation postponed successfully']"));
        return successfulPostponeMessage.isDisplayed();
    }

    public boolean isErrorMessageDisplayed(){
        waitUtils.waitForElementToBeVisible(By.xpath("//div[normalize-space(text())='Please fill in all required fields']"));
        return reservationError.isDisplayed();
    }

    public boolean isNonExistCustomerMessageDisplayed(){
        waitUtils.waitForElementToBeVisible(By.xpath("//div[@class='mb-4 p-3 bg-red-50 border border-red-200 text-red-600 rounded-lg text-sm']"));
        return inavlidCustomer.isDisplayed();
    }

    public boolean isInvalidErrorMessageDisplayed(){
        waitUtils.waitForElementToBeVisible(By.xpath("//div[normalize-space(text())='Please enter a valid email address']"));
        return invalidEmailMessage.isDisplayed();
    }


}
