package com.propfind.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.propfind.base.BaseTestClass;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TestNG listener that builds an Extent Reports HTML report.
 * Registered in test.xml — no changes needed in test classes.
 *
 * Screenshot capture works for plain TestNG tests that extend BaseTestClass.
 * For Cucumber scenarios, screenshots are attached directly via scenario.attach()
 * in each feature's Hooks class (@After hook) — that path is intentional and
 * independent of this listener.
 *
 * Output: target/extent-reports/ExtentReport_<timestamp>.html
 */
public class ExtentReportListener implements ITestListener {

    private static ExtentReports extent;
    // One ExtentTest node per thread so parallel runs don't collide
    private static final ThreadLocal<ExtentTest> testNode = new ThreadLocal<>();

    // ── Suite lifecycle ───────────────────────────────────────────────────────

    @Override
    public void onStart(ITestContext context) {
        if (extent != null) return; // already initialised by a prior <test> block

        String timestamp  = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String reportDir  = "target/extent-reports";
        String reportFile = reportDir + "/ExtentReport_" + timestamp + ".html";

        new File(reportDir).mkdirs();

        ExtentSparkReporter spark = new ExtentSparkReporter(reportFile);
        spark.config().setDocumentTitle("PropFind – Test Report");
        spark.config().setReportName("PropFind Selenium Suite");
        spark.config().setTheme(Theme.STANDARD);
        spark.config().setEncoding("UTF-8");

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Project",     "PropFind");
        extent.setSystemInfo("Environment", "Local file://");
        extent.setSystemInfo("Browser",     "Chrome");
        extent.setSystemInfo("Java",        System.getProperty("java.version"));
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
        }
    }

    // ── Test lifecycle ────────────────────────────────────────────────────────

    @Override
    public void onTestStart(ITestResult result) {
        String name        = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        String[] groups    = result.getMethod().getGroups();

        ExtentTest node = extent.createTest(name, description);
        for (String g : groups) node.assignCategory(g);
        testNode.set(node);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        testNode.get().log(Status.PASS, "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest node = testNode.get();
        node.log(Status.FAIL, result.getThrowable());

        // Attach screenshot only for plain TestNG tests that extend BaseTestClass.
        // Cucumber scenario screenshots are handled in each feature's Hooks (@After).
        WebDriver driver = getDriverFromTestNG(result);
        if (driver instanceof TakesScreenshot ts) {
            try {
                byte[] png = ts.getScreenshotAs(OutputType.BYTES);
                String b64 = java.util.Base64.getEncoder().encodeToString(png);
                node.addScreenCaptureFromBase64String(b64, "Failure screenshot");
            } catch (Exception ignored) {
                node.log(Status.WARNING, "Could not capture screenshot: " + ignored.getMessage());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (result.getThrowable() != null) {
            testNode.get().log(Status.SKIP, result.getThrowable());
        } else {
            testNode.get().log(Status.SKIP, "Test skipped");
        }
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    /**
     * Retrieves the WebDriver from the test instance when it extends BaseTestClass
     * (plain TestNG tests). Returns null for Cucumber runners —
     * those attach screenshots via scenario.attach() in their Hooks class instead.
     */
    private WebDriver getDriverFromTestNG(ITestResult result) {
        Object instance = result.getInstance();
        if (instance instanceof BaseTestClass base) {
            return base.getDriver();
        }
        return null;
    }
}
