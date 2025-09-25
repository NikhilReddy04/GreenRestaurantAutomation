package org.epam.devbistro.stepdefinitions.ui;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.epam.devbistro.utils.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

public class NxtWaveSteps {

    WebDriver driver= DriverManager.getInstance().getDriver();

    @Given("user is on Nxtwave LoginPage")
    public void userIsOnLoginPage(){
        driver.get("https://nikhtrendz.ccbp.tech/login");
    }

    @When("User submits valid credentials")
    public void userSubmitsValidCredentials() {
        driver.findElement(By.xpath("//input[@id='username']")).sendKeys("rahul");
        driver.findElement(By.xpath("//input[@id='password']")).sendKeys("rahul@2021");
        driver.findElement(By.xpath("//button[normalize-space()='Login']")).click();
    }

    @Then("User should login")
    public void userShouldLogin() {
        Assert.assertTrue(driver.findElement(By.xpath("//button[normalize-space()='Shop Now']")).isDisplayed());
    }

    @Then("User should not login")
    public void userShouldNotLogin() {
        Assert.assertTrue(driver.findElement(By.xpath("//p[@class='error-message']")).isDisplayed());
    }

    @When("User submits invalid credentials")
    public void userSubmitsInvalidCredentials() {
        driver.findElement(By.xpath("//input[@id='username']")).sendKeys("rahul");
        driver.findElement(By.xpath("//input[@id='password']")).sendKeys("ral@2021");
        driver.findElement(By.xpath("//button[normalize-space()='Login']")).click();

    }
}
