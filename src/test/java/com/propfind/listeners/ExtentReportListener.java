package com.propfind.listeners;

import com.propfind.base.BaseTestClass;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG listener that builds an Extent Reports HTML report.
 * Registered in test.xml — no changes needed in test classes.
 *
 * <h3>Cucumber vs plain TestNG tests</h3>
 * When Cucumber runs via {@code AbstractTestNGCucumberTests}, every scenario
 * is reported as a TestNG method named {@code runScenario}.  That generic name
 * is useless in the report.  Instead, {@link CucumberExtentHooks} creates a
 * properly named, tagged node for every Cucumber scenario via the shared
 * {@link ExtentReportManager} — this listener skips those calls entirely.
 *
 * For <em>plain TestNG</em> tests (classes that extend {@link BaseTestClass}),
 * this listener still creates named nodes using the method name and groups.
 *
 * Output: {@code target/extent-reports/ExtentReport_<timestamp>.html}
 */
public class ExtentReportListener implements ITestListener {

    // ── Suite lifecycle ───────────────────────────────────────────────────────

    @Override
    public void onStart(ITestContext context) {
        // Delegate initialisation to the shared manager (no-op if already done).
        ExtentReportManager.init();
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReportManager.flush();
    }

    // ── Test lifecycle ────────────────────────────────────────────────────────

    @Override
    public void onTestStart(ITestResult result) {
        // Skip Cucumber runner methods — CucumberExtentHooks handles those.
        if (isCucumberRunner(result)) return;

        String name        = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        String[] groups    = result.getMethod().getGroups();

        ExtentReportManager.startTest(name, description, groups);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        if (isCucumberRunner(result)) return;
        ExtentReportManager.passTest("Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        if (isCucumberRunner(result)) return;

        ExtentReportManager.failTest(result.getThrowable());

        // Attach screenshot for plain TestNG tests that extend BaseTestClass.
        WebDriver driver = getDriverFromTestNG(result);
        if (driver instanceof TakesScreenshot ts) {
            try {
                byte[] png = ts.getScreenshotAs(OutputType.BYTES);
                String b64 = java.util.Base64.getEncoder().encodeToString(png);
                ExtentReportManager.attachScreenshot(b64, "Failure screenshot");
            } catch (Exception ignored) {
                ExtentReportManager.failTest("Could not capture screenshot: " + ignored.getMessage());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (isCucumberRunner(result)) return;

        if (result.getThrowable() != null) {
            ExtentReportManager.skipTest(result.getThrowable().getMessage());
        } else {
            ExtentReportManager.skipTest("Test skipped");
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /**
     * Returns true when the TestNG result belongs to a Cucumber runner.
     * Cucumber's {@code AbstractTestNGCucumberTests} calls every scenario as a
     * method named {@code runScenario} — that's the reliable discriminator.
     */
    private static boolean isCucumberRunner(ITestResult result) {
        return "runScenario".equals(result.getMethod().getMethodName());
    }

    /**
     * Retrieves the WebDriver from the test instance when it extends
     * {@link BaseTestClass} (plain TestNG tests).
     * Returns null for Cucumber runners.
     */
    private static WebDriver getDriverFromTestNG(ITestResult result) {
        Object instance = result.getInstance();
        if (instance instanceof BaseTestClass base) {
            return base.getDriver();
        }
        return null;
    }
}
