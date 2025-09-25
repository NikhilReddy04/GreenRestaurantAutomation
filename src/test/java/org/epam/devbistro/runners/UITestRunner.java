package org.epam.devbistro.runners;

import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features/ui/nxtwave.feature", // Path to the UI feature file
        plugin = {
                "pretty",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm", // Allure plugin
                "html:target/ui-cucumber-reports.html", // UI-specific HTML report
                "json:target/ui-cucumber.json" ,// UI-specific JSON report
        }
)
public class UITestRunner extends BaseTestRunner {
    // UI-specific configurations (if any) can go here

}