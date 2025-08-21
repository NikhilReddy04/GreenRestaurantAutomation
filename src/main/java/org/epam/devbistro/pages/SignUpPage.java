package org.epam.devbistro.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignUpPage extends BasePage{
    private WebDriver driver;
    @FindBy(xpath = "//input[@placeholder='Enter your First Name']")
    private WebElement firstNameField;
    @FindBy(xpath = "//input[@placeholder='Enter your Last Name']")
    private WebElement lastNameField;

    @FindBy(xpath = "//input[@placeholder='Enter your Email']")
    private WebElement emailField;

    @FindBy(xpath = "//input[@placeholder='Enter your Password']")
    private WebElement passwordField;

    @FindBy(xpath = "//input[@placeholder='Confirm New Password']")
    private WebElement confirmPasswordField;


   @FindBy(xpath = "//button[normalize-space()='Create an Account']")
   private WebElement signupButton;


    //@FindBy(xpath = "//button[@placeholder='Email is required']")
    //private WebElement errorEmailMessage;

    public SignUpPage(WebDriver driver) {
        super(driver);
    }

    public void enterFirstName(String firstName) {
        firstNameField.clear();
        firstNameField.sendKeys(firstName);
    }

    public void enterLastName(String lastName) {
        lastNameField.clear();
        lastNameField.sendKeys(lastName);
    }

    public void enterEmail(String email) {
        emailField.clear();
        emailField.sendKeys(email);
    }

    public void enterPassword(String password) {
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void enterConfirmPassword(String confirmPassword) {
        confirmPasswordField.clear();
        confirmPasswordField.sendKeys(confirmPassword);
    }

    public void clickSignUp() {
        signupButton.click();
    }
    public String errorMessage() {
        try {
            WebElement toast = driver.findElement(By.xpath("//div[contains(@class,'Toastify__toast--error')]"));
            return toast.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }


}






