package org.epam.devbistro.stepdefinitions.ui;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.epam.devbistro.pages.LocationPage;
import org.epam.devbistro.utils.ConfigReader;
import org.epam.devbistro.utils.DriverManager;
import org.epam.devbistro.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.testng.Assert.assertTrue;

public class LocationsUiSteps {
   // private WebDriver driver= DriverManager.getInstance().getDriver();
   private WebDriver driver=new ChromeDriver();
    private LocationPage locationPage = new LocationPage(driver);
    WaitUtils waitUtils=new WaitUtils(driver);


    @When("user goes to Location Page")
    public void UserGoesToLocationPage(){
       // waitUtils.waitForElementToBeVisible(By.xpath("//p[contains(text(), '124 Main Street')]/ancestor::div[contains(@class, 'cursor-pointer')]"));
        locationPage.clickLocation();
    }
  @And("clicks on Book Table")
    public void ClicksOnBookTable(){
        waitUtils.waitForElementClickable(By.xpath("//button[normalize-space(text())='Book a Table']"));
        locationPage.clickBookTable();
  }


    @Given("the user is the home page")
    public void theUserIsTheHomePage() {
        //String baseUrl = ConfigReader.get("BASE_URL").replaceAll("/$", "");
        driver.get("http://team-15-frontend-bucket.s3-website-ap-southeast-1.amazonaws.com");
    }

    @Then("user should see Booking Page")
    public void userShouldSeeBookingPage() {
       boolean isDisplayed=locationPage.isBookingHeaderDisplayed();
        assertTrue(isDisplayed,"Book a Table is not displayed!");
    }
}
