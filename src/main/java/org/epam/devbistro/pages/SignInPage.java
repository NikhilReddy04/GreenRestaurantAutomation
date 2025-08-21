package org.epam.devbistro.pages;

import org.epam.devbistro.utils.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.testng.Assert.assertTrue;

public class SignInPage extends BasePage{
    @FindBy(xpath = "//input[@placeholder='Enter your Email']")
    private WebElement emailField;
    @FindBy(xpath = "//input[@placeholder='Enter your Password']")
    private WebElement passwordFeild;
    @FindBy(xpath = "//button[text()='Sign In']")
    private WebElement signin;


    public SignInPage(WebDriver driver) {
        super(driver);
    }

    //actions
    public void enterEmail(String email) {
        emailField.sendKeys(email);
    }

    public void enterPassword(String password) {
        passwordFeild.sendKeys(password);
    }

    public void clickLogin() {
        signin.click();
    }

    public String HomePageElement() {
      //  WebElement homeelements = driver.findElement(By.xpath("//h2[text()='Green & Tasty']"));
     String homeTitle=driver.getTitle();
     return homeTitle;
    }

    public void loginAndRedirectToBookings(){
      enterEmail(ConfigReader.get("email"));
      enterPassword(ConfigReader.get("password"));
       clickLogin();

       // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String baseUrl = ConfigReader.get("BASE_URL").replaceAll("/$", "");
        driver.get(baseUrl + "/book");



    }
}
