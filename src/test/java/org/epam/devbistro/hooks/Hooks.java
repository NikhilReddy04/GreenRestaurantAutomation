package org.epam.devbistro.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.epam.devbistro.utils.DriverManager;
import org.epam.devbistro.utils.ConfigReader;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Hooks {


    @Before("@ui")
    public void setUp() {
        System.out.println("Beginning ui");
        // Initialize WebDriver using DriverManager
        DriverManager.getInstance().setDriver();
        WebDriver driver = DriverManager.getInstance().getDriver();


        // Navigate to base URL
        String baseUrl = ConfigReader.get("BASE_URL");
        driver.get(baseUrl);

        // Maximize browser window and set implicit wait
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        System.out.println("Browser is initialized successfully!");

    }

    @After("@ui")
   //@After("@sample")
    public void tearDown(Scenario scenario) throws IOException {
        WebDriver driver = DriverManager.getInstance().getDriver();
        if (scenario.isFailed() && driver instanceof TakesScreenshot) {
            try {
                // Capture screenshot as bytes
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

                // Attach to Allure report
                scenario.attach(screenshot, "image/png", "Screenshot on failure");

                System.out.println("Screenshot attached to Allure report for failed scenario: " + scenario.getName());

            } catch (Exception e) {
                System.err.println("Failed to capture screenshot: " + e.getMessage());
            }
        }
        DriverManager.getInstance().quitDriver();  // Proper cleanup of ThreadLocal
        System.out.println("Browser quitted");
    }

}
