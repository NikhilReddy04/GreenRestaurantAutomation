package org.epam.devbistro.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class FamousLocationsPage extends BasePage {

    @FindBy(css = "div.location-id")
    private List<WebElement> locationIds;

    @FindBy(css = "div.restaurant-name")
    private List<WebElement> restaurantNames;

    @FindBy(css = "div.recent-review")
    private List<WebElement> recentReviews;

    public FamousLocationsPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public List<WebElement> getLocationIds() {
        return locationIds;
    }

    public List<WebElement> getRestaurantNames() {
        return restaurantNames;
    }

    public List<WebElement> getRecentReviews() {
        return recentReviews;
    }
}
