package com.propfind.context;

import com.propfind.config.SiteConfig;
import com.propfind.driver.DriverFactory;
import com.propfind.pages.ComparePage;
import org.openqa.selenium.WebDriver;

/**
 * Cucumber PicoContainer context — one instance per scenario.
 * Holds the WebDriver and ComparePage so they can be injected into
 * both CompareSteps and CompareHooks without static state.
 *
 * The driver is created lazily on first access so that dry-runs or
 * tag-filtered scenarios that skip all steps never open a browser.
 */
public class CompareTestContext {

    private WebDriver driver;
    private ComparePage comparePage;

    /** Lazily initialises the driver and page on first call. */
    public WebDriver getDriver() {
        if (driver == null) {
            driver      = DriverFactory.createChromeDriver();
            comparePage = new ComparePage(driver);
        }
        return driver;
    }

    public ComparePage getComparePage() {
        getDriver();
        return comparePage;
    }

    public static String getBaseUrl() {
        return SiteConfig.resolveBaseUrl();
    }
}
