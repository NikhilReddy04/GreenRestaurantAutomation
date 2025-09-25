package org.epam.devbistro.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        glue ={"org.epam.devbistro.stepdefinitions",
                "org.epam.devbistro.hooks"},
        // Package for step definitions
        plugin = {
                "pretty", // Prints readable output
                "html:target/cucumber-reports.html", // Generates default HTML report
                "json:target/cucumber.json" ,// Generates default JSON report
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"  // ExtentReports adapter
        },
        monochrome = true // Makes console output more readable
)

public abstract class BaseTestRunner extends AbstractTestNGCucumberTests {
        @Override
        @DataProvider(parallel = true)
        public Object[][] scenarios() {
                return super.scenarios();
        }
}
