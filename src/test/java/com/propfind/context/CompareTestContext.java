package com.propfind.context;

import com.propfind.driver.DriverFactory;
import com.propfind.pages.ComparePage;
import org.openqa.selenium.WebDriver;

import java.io.File;

/**
 * Cucumber PicoContainer context — one instance per scenario.
 * Holds the WebDriver and ComparePage so they can be injected into
 * both CompareSteps and CompareHooks without static state.
 */
public class CompareTestContext {

    private final WebDriver driver;
    private final ComparePage comparePage;
    private static String baseUrl;

    public CompareTestContext() {
        if (baseUrl == null || baseUrl.isBlank()) {
            File siteRoot = new File("propfind-website");
            baseUrl = siteRoot.toURI().toString();
        }
        driver      = DriverFactory.createChromeDriver();
        comparePage = new ComparePage(driver);
    }

    public WebDriver getDriver()           { return driver; }
    public ComparePage getComparePage()    { return comparePage; }
    public static String getBaseUrl()      { return baseUrl; }
}
