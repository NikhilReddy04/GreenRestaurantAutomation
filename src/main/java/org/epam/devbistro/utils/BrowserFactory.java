package org.epam.devbistro.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class BrowserFactory {
    public static WebDriver createInstance(String browserName){
        WebDriver driver;
        switch(browserName.toLowerCase())
        {
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver=new EdgeDriver();
                break;
            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver=new ChromeDriver();
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver=new FirefoxDriver();
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browserName);

        }
        driver.manage().window().maximize();
        return driver;

    }
}

