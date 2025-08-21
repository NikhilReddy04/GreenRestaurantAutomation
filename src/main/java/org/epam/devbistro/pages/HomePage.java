package org.epam.devbistro.pages;

import org.epam.devbistro.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.testng.Assert.assertTrue;

public class HomePage extends BasePage {


    @FindBy(xpath = "//button[normalize-space()='View Menu']")
    private WebElement viewMenuButton;

    @FindBy(xpath = "//a[normalize-space()='Book a Table']")
    private WebElement bookTableLink;

    @FindBy(xpath = "//a[normalize-space()='Reservation']")
    private WebElement reservationsLink;

    @FindBy(xpath = "//span[text()='Cuisine Experience']")
    private WebElement cuisineExperienceButton;

    @FindBy(xpath = "//p[normalize-space()='123 Main Street, Downtown']")
    private WebElement location123;

    @FindBy(xpath = "//p[text()='Downtown Italian Restaurant']")
    private WebElement location123Name;


    public HomePage(WebDriver driver) {
        super(driver);
    }

    //actions
    public void clickViewMenuButton() {
        viewMenuButton.click();
    }

    public void clickBookTableButton() {
        bookTableLink.click();
    }

    public void clickReservationsButton() {
        reservationsLink.click();
    }

    public void navigateToLocationWithId(String id){
        waitUtils.waitForElementToBeVisible(By.xpath("//p[normalize-space()='123 Main Street, Downtown']"));
        location123.click();
    }

    public boolean isLocationNameVisible(String locationName){
        waitUtils.waitForElementToBeVisible(By.xpath("//p[text()='Downtown Italian Restaurant']"));
        return location123Name.isDisplayed();
    }



    public void isServiceFeedbackPresentWith(String feedback){
        WebElement mainPageServiceFeedback=driver.findElement(By.xpath("//p[normalize-space()='"+feedback+"']"));
        mainPageServiceFeedback.isDisplayed();
    }

    public void clickCuisineExperienceTab(){
        cuisineExperienceButton.click();
    }

    public void isCuisineFeedbackPresentWith(String feedback){
        WebElement mainPageCuisineFeedback=driver.findElement(By.xpath("//p[normalize-space()='"+feedback+"']"));
        mainPageCuisineFeedback.isDisplayed();
    }

}
