package com.propfind.context;

import com.propfind.config.SiteConfig;
import com.propfind.driver.DriverFactory;
import com.propfind.pages.DashboardPage;
import org.openqa.selenium.WebDriver;

/**
 * Cucumber PicoContainer context — one instance per scenario.
 * Holds the WebDriver and DashboardPage so they can be injected into
 * both DashboardSteps and DashboardHooks without static state.
 *
 * The driver is created lazily on first access so that dry-runs or
 * tag-filtered scenarios that skip all steps never open a browser.
 */
public class DashboardTestContext {

    private WebDriver driver;
    private DashboardPage dashboardPage;

    /** Lazily initialises the driver and page on first call. */
    public WebDriver getDriver() {
        if (driver == null) {
            driver        = DriverFactory.createChromeDriver();
            dashboardPage = new DashboardPage(driver);
        }
        return driver;
    }

    public DashboardPage getDashboardPage() {
        getDriver();
        return dashboardPage;
    }

    public static String getBaseUrl() {
        return SiteConfig.resolveBaseUrl();
    }
}
