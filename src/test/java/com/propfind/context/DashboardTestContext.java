package com.propfind.context;

import com.propfind.driver.DriverFactory;
import com.propfind.pages.DashboardPage;
import org.openqa.selenium.WebDriver;

import java.io.File;

/**
 * Cucumber PicoContainer context — one instance per scenario.
 * Holds the WebDriver and DashboardPage so they can be injected into
 * both DashboardSteps and DashboardHooks without static state.
 */
public class DashboardTestContext {

    private final WebDriver driver;
    private final DashboardPage dashboardPage;
    private static String baseUrl;

    public DashboardTestContext() {
        if (baseUrl == null || baseUrl.isBlank()) {
            File siteRoot = new File("propfind-website");
            baseUrl = siteRoot.toURI().toString();
        }

        driver        = DriverFactory.createChromeDriver();
        dashboardPage = new DashboardPage(driver);
    }

    public WebDriver getDriver()              { return driver; }
    public DashboardPage getDashboardPage()   { return dashboardPage; }
    public static String getBaseUrl()         { return baseUrl; }
}
