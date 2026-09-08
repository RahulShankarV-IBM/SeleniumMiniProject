package com.propfind.base;

import com.propfind.driver.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.io.File;

/**
 * Base class for plain TestNG tests (non-Cucumber).
 * Cucumber-based tests use their own TestContext + LoginHooks instead.
 */
public class BaseTestClass {

    protected WebDriver driver;
    protected String baseUrl;

    /**
     * Receives the optional baseUrl parameter from test.xml.
     * Falls back to the local TargetWebsite copy bundled in the project root.
     */
    @BeforeMethod
    @Parameters("baseUrl")
    public void setUp(@Optional("") String baseUrl) {
        if (baseUrl == null || baseUrl.isBlank()) {
            // Resolve the bundled HTML site from the project-root propfind-website folder
            File siteRoot = new File("propfind-website");
            this.baseUrl = siteRoot.toURI().toString();
        } else {
            this.baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
        }

        driver = DriverFactory.createChromeDriver();
    }

    /** Exposes the driver to listeners (e.g. ExtentReportListener) outside this package. */
    public WebDriver getDriver() {
        return driver;
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /** Convenience helper: build a full file URL for a given HTML page name. */
    protected String pageUrl(String htmlFileName) {
        return baseUrl + htmlFileName;
    }

    /**
     * Captures a screenshot as a Base64-encoded PNG string.
     * Returns null if the driver is not available or does not support screenshots.
     * Used by ExtentReportListener; can also be called directly in test methods.
     */
    protected String captureScreenshotAsBase64() {
        if (driver instanceof org.openqa.selenium.TakesScreenshot ts) {
            return java.util.Base64.getEncoder().encodeToString(
                    ts.getScreenshotAs(org.openqa.selenium.OutputType.BYTES));
        }
        return null;
    }
}
