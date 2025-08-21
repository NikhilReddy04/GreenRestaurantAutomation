package org.epam.devbistro.pages;
import org.epam.devbistro.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Map;
import java.util.List;

public class ReservationPage extends BasePage {
    private static final Map<String, List<String>> TIME_SLOT_MAP = Map.of(
            "slot1", List.of("10:30", "12:00"),
            "slot2", List.of("12:15", "13:45"),
            "slot3", List.of("14:00", "15:30"),
            "slot4", List.of("15:45", "17:15"),
            "slot5", List.of("17:30", "19:00"),
            "slot6", List.of("19:15", "20:45"),
            "slot7", List.of("21:00", "22:30")
    );

    @FindBy(xpath = "//p[starts-with(text(),'Hello')]")
    private WebElement helloUserTextElement;

    @FindBy(xpath = "//button[normalize-space()='Update Feedback']")
    private WebElement updateFeedbackButton;

    @FindBy(xpath = "//button[normalize-space()='Service']")
    private WebElement serviceButton;

    @FindBy(xpath = "//button[normalize-space()='Culinary Experience']")
    private WebElement culinaryExperience;

    @FindBy(xpath = "//textarea[@placeholder='Share your thoughts about the service (optional)']")
    private WebElement serviceFeedbackEntryField;

    @FindBy(xpath = "//textarea[@placeholder='Share your thoughts about the cuisine (optional)']")
    private WebElement cuisineFeedbackEntryField;

    @FindBy(xpath = "//button[normalize-space()='Edit Feedback']")
    private WebElement innerEditFeedbackButton;

    @FindBy(xpath = "//textarea/../../..//button[normalize-space()='Update Feedback']")
    private WebElement innerUpdateFeedbackButton;

    @FindBy(xpath = "//div[normalize-space='Feedback updated successfully']")
    private WebElement feedbackUpdatedSuccessfullyConfirmationMessage;

    @FindBy(xpath = "//a[normalize-space()='Main Page']")
    private WebElement mainPageButton;






    public ReservationPage(WebDriver driver) {
        super(driver);

    }




    //Methods
    public boolean isHelloUserTextElementDisplayed(){
        return helloUserTextElement.isDisplayed();
    }

    public String getSlotNumber(String confirmationMessage) {
        // Extract time from the confirmationMessage
        String[] parts = confirmationMessage.split("from ")[1].split(" at ")[0].split(" to ");
        String fromTime = parts[0].trim();
        String toTime = parts[1].trim();

        for (Map.Entry<String, List<String>> entry : TIME_SLOT_MAP.entrySet()) {
            List<String> slotTimes = entry.getValue();
            if (slotTimes.get(0).equals(fromTime) && slotTimes.get(1).equals(toTime)) {
                return entry.getKey();
            }
        }

        return "Slot not found";
    }


    public boolean isFeedbackDetailMessageDisplayed(){
        WebElement feedbackDetailsMessage = driver.findElement(By.xpath("//h2[normalize-space()='Feedback Details']"));
        return feedbackDetailsMessage.isDisplayed();
    }

    public boolean isEditFeedbackTextMessageDisplayed(){
        WebElement feedbackDetailsMessage = driver.findElement(By.xpath("//h2[normalize-space()='Edit Feedback']"));
        return feedbackDetailsMessage.isDisplayed();
    }

    public void clickUpdateFeedbackButton(){
        updateFeedbackButton.click();
    }

    public void setServiceRatingTo(int stars){
        String xpath="//h3[text()='Service']/following-sibling::div//button["+stars+"]";
        driver.findElement(By.xpath(xpath)).click();
    }

    public void clickCulinaryExperience(){
        culinaryExperience.click();
    }

    public void setCuisineRatingTo(int stars){
        String xpath="//h3[text()='Cuisine']/following-sibling::div//button["+stars+"]";
        driver.findElement(By.xpath(xpath)).click();
    }

    public void clickEditFeedbackButton(){
        innerEditFeedbackButton.click();
    }

    public void setServiceComment(String serviceComment){
        serviceFeedbackEntryField.clear();
        serviceFeedbackEntryField.sendKeys(serviceComment);
    }

    public void setCuisineComment(String cuisineComment){
        cuisineFeedbackEntryField.clear();
        cuisineFeedbackEntryField.sendKeys(cuisineComment);
    }

    public void clickInnerUpdateFeedbackButton(){
        innerUpdateFeedbackButton.click();
    }

    public boolean isSuccessfulFeedbackMessageVisible(){
        driver.findElement(By.xpath("//div[normalize-space()='Feedback updated successfully']"));
        return feedbackUpdatedSuccessfullyConfirmationMessage.isDisplayed();
    }

    public void clickMainPageButton(){
        mainPageButton.click();
    }

}