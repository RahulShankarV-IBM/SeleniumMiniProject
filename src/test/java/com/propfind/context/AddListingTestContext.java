package com.propfind.context;

import com.propfind.config.SiteConfig;
import com.propfind.driver.DriverFactory;
import com.propfind.pages.AddListingPage;
import org.openqa.selenium.WebDriver;

/**
 * Cucumber PicoContainer context — one instance per scenario.
 * Holds the WebDriver and AddListingPage so they can be injected into
 * both AddListingSteps and AddListingHooks without static state.
 *
 * The driver is created lazily on first access so that dry-runs or
 * tag-filtered scenarios that skip all steps never open a browser.
 */
public class AddListingTestContext {

    private WebDriver driver;
    private AddListingPage addListingPage;

    /** Lazily initialises the driver and page on first call. */
    public WebDriver getDriver() {
        if (driver == null) {
            driver         = DriverFactory.createChromeDriver();
            addListingPage = new AddListingPage(driver);
        }
        return driver;
    }

    public AddListingPage getAddListingPage() {
        getDriver();
        return addListingPage;
    }

    public static String getBaseUrl() {
        return SiteConfig.resolveBaseUrl();
    }
}
