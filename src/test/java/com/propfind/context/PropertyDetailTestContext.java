package com.propfind.context;

import com.propfind.driver.DriverFactory;
import com.propfind.pages.PropertyDetailPage;
import org.openqa.selenium.WebDriver;

import java.io.File;

/**
 * Cucumber PicoContainer context — one instance per scenario.
 * Holds the WebDriver and PropertyDetailPage so they can be injected into
 * both PropertyDetailSteps and PropertyDetailHooks without static state.
 */
public class PropertyDetailTestContext {

    private final WebDriver driver;
    private final PropertyDetailPage page;
    private static String baseUrl;

    public PropertyDetailTestContext() {
        if (baseUrl == null || baseUrl.isBlank()) {
            File siteRoot = new File("propfind-website");
            baseUrl = siteRoot.toURI().toString();
        }
        driver = DriverFactory.createChromeDriver();
        page   = new PropertyDetailPage(driver);
    }

    public WebDriver getDriver()                          { return driver; }
    public PropertyDetailPage getPropertyDetailPage()     { return page; }
    public static String getBaseUrl()                     { return baseUrl; }
}
