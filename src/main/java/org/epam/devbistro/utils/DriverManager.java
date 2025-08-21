package org.epam.devbistro.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DriverManager {

    private static final ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();

    private DriverManager() {}

    private static class Holder {
        private static final DriverManager INSTANCE = new DriverManager();
    }

    public static DriverManager getInstance() {
        return Holder.INSTANCE;
    }

    public void setDriver() {
        String browser = ConfigReader.get("browser");           // "chrome","microsoftEdge","firefox"
        String runMode = ConfigReader.get("runMode");           // "local", "remote", "browserstack"
        String browserstackUser = ConfigReader.get("bsUser");   // BrowserStack username
        String browserstackKey = ConfigReader.get("bsKey");     // BrowserStack access key

        System.out.println("Browser: " + browser);

        if (browser == null || browser.isEmpty()) {
            throw new RuntimeException("Browser is not specified in config.properties");
        }

        if ("remote".equalsIgnoreCase(runMode)) {
            try {
                DesiredCapabilities capabilities = new DesiredCapabilities();
                capabilities.setBrowserName(browser);

                WebDriver remoteDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities);
                threadLocalDriver.set(remoteDriver);

            } catch (MalformedURLException e) {
                throw new RuntimeException("Invalid Selenium Grid URL", e);
            }

        } else if ("browserstack".equalsIgnoreCase(runMode)) {
            try {
                DesiredCapabilities capabilities = new DesiredCapabilities();
                capabilities.setCapability("browserName", "Chrome");
                capabilities.setCapability("browserVersion", "latest");

                Map<String, Object> bstackOptions = new HashMap<>();
                bstackOptions.put("os", "Windows");
                bstackOptions.put("osVersion", "10");
                bstackOptions.put("buildName", "DevBistro Build");
                bstackOptions.put("sessionName", "Search Test");

                capabilities.setCapability("bstack:options", bstackOptions);


                WebDriver bsDriver = new RemoteWebDriver(
                        new URL("https://" + browserstackUser + ":" + browserstackKey + "@hub-cloud.browserstack.com/wd/hub")
                        ,

                        capabilities
                );
                threadLocalDriver.set(bsDriver);

            } catch (MalformedURLException e) {
                throw new RuntimeException("Invalid BrowserStack URL", e);
            }

        } else { // local
            WebDriver driver = BrowserFactory.createInstance(browser);
            threadLocalDriver.set(driver);
        }
    }

    public WebDriver getDriver() {
        return threadLocalDriver.get();
    }

    public void quitDriver() {
        WebDriver driver = threadLocalDriver.get();
        if (driver != null) {
            driver.quit();
            threadLocalDriver.remove();
        }
    }
}
