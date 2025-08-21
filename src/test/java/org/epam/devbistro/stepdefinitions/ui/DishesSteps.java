package org.epam.devbistro.stepdefinitions.ui;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.epam.devbistro.pages.*;
import org.epam.devbistro.utils.ConfigReader;
import org.epam.devbistro.utils.DriverManager;
import org.epam.devbistro.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import static junit.framework.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class DishesSteps {

    private WebDriver driver= DriverManager.getInstance().getDriver();
    HomePage homePage=new HomePage(driver);
    DishesPage dishesPage=new DishesPage(driver);
    @Given("the user is on the home page")
    public void theUserIsOnTheHomePage() {
        String baseUrl = ConfigReader.get("BASE_URL").replaceAll("/$", "");
        driver.get(baseUrl);
    }

    @When("The user clicks on view menu button")
    public void theUserClicksOnViewMenuButton() {
        homePage.clickViewMenuButton();
    }

    @Then("the user should see the available dishes on the page")
    public void theUserShouldSeeTheAvailableDishesInThePage() {
        dishesPage.clickMenuTypeButton(DishType.APPETIZER);
        dishesPage.clickMenuTypeButton(DishType.DESSERT);
        dishesPage.clickMenuTypeButton(DishType.MAIN_COURSE);

        dishesPage.selectSortingOrder(SortingType.LEAST_POPULAR);
        dishesPage.selectSortingOrder(SortingType.MOST_POPULAR);
        dishesPage.selectSortingOrder(SortingType.LOWER_PRICE);
        dishesPage.selectSortingOrder(SortingType.MORE_PRICE);
    }
}
