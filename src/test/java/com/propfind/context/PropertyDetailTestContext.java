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
    private final PropertyDetailPage propertyDetailPage;
    private static String baseUrl;

    public PropertyDetailTestContext() {
        if (baseUrl == null || baseUrl.isBlank()) {
            File siteRoot = new File("propfind-website");
            baseUrl = siteRoot.toURI().toString();
        }
        driver             = DriverFactory.createChromeDriver();
        propertyDetailPage = new PropertyDetailPage(driver);
    }

    public WebDriver getDriver()                          { return driver; }
    public PropertyDetailPage getPropertyDetailPage()     { return propertyDetailPage; }
    public static String getBaseUrl()                     { return baseUrl; }
}
