package org.epam.devbistro.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LocationPage extends BasePage {


    @FindBy(xpath= "//p[contains(text(), '124 Main Street')]/ancestor::div[contains(@class, 'cursor-pointer')]")
    private WebElement location;

    @FindBy(xpath="//button[contains(text(),'Book a Table')]")
    private WebElement bookTableButton;

    @FindBy(xpath="//h3[normalize-space('Book a Table')]")
    private WebElement bookingHeader;

    public LocationPage(WebDriver driver) {
        super(driver);
    }

    public void clickLocation(){
        waitUtils.waitForElementToBeVisible(By.xpath("//p[contains(text(), '124 Main Street')]/ancestor::div[contains(@class, 'cursor-pointer')]"));
        location.click();
    }

    public void clickBookTable(){
        bookTableButton.click();

    }
    public boolean isBookingHeaderDisplayed(){
        waitUtils.waitForElementToBeVisible(By.xpath("//h3[normalize-space('Book a Table')]"));
        return bookingHeader.isDisplayed();
    }



}
