package org.epam.devbistro.Listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class BrowserListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        String browser = context.getCurrentXmlTest().getParameter("browser");
        if (browser != null) {
            System.setProperty("browser", browser);
            System.out.println("Browser set from XML: " + browser);
        } else {
            System.out.println("No browser parameter found in testng.xml");
        }
    }

    @Override public void onFinish(ITestContext context) {}
    @Override public void onTestStart(ITestResult result) {}
    @Override public void onTestSuccess(ITestResult result) {}
    @Override public void onTestFailure(ITestResult result) {}
    @Override public void onTestSkipped(ITestResult result) {}
    @Override public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}
    @Override public void onTestFailedWithTimeout(ITestResult result) {}
}
