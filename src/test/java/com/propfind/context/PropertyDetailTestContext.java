package com.propfind.context;

import com.propfind.config.SiteConfig;
import com.propfind.driver.DriverFactory;
import com.propfind.pages.PropertyDetailPage;
import org.openqa.selenium.WebDriver;

/**
 * Cucumber PicoContainer context — one instance per scenario.
 * Holds the WebDriver and PropertyDetailPage so they can be injected into
 * both PropertyDetailSteps and PropertyDetailHooks without static state.
 *
 * The driver is created lazily on first access so that dry-runs or
 * tag-filtered scenarios that skip all steps never open a browser.
 */
public class PropertyDetailTestContext {

    private WebDriver driver;
    private PropertyDetailPage propertyDetailPage;

    /** Lazily initialises the driver and page on first call. */
    public WebDriver getDriver() {
        if (driver == null) {
            driver             = DriverFactory.createChromeDriver();
            propertyDetailPage = new PropertyDetailPage(driver);
        }
        return driver;
    }

    public PropertyDetailPage getPropertyDetailPage() {
        getDriver();
        return propertyDetailPage;
    }

    public static String getBaseUrl() {
        return SiteConfig.resolveBaseUrl();
    }
}
