package com.propfind.context;

import com.propfind.config.SiteConfig;
import com.propfind.driver.DriverFactory;
import com.propfind.pages.MapViewPage;
import org.openqa.selenium.WebDriver;

/**
 * Cucumber PicoContainer context — one instance per scenario.
 * Holds the WebDriver and MapViewPage so they can be injected into
 * both MapViewSteps and MapViewHooks without static state.
 *
 * The driver is created lazily on first access so that dry-runs or
 * tag-filtered scenarios that skip all steps never open a browser.
 */
public class MapViewTestContext {

    private WebDriver driver;
    private MapViewPage mapViewPage;

    /** Lazily initialises the driver and page on first call. */
    public WebDriver getDriver() {
        if (driver == null) {
            driver      = DriverFactory.createChromeDriver();
            mapViewPage = new MapViewPage(driver);
        }
        return driver;
    }

    public MapViewPage getMapViewPage() {
        getDriver();
        return mapViewPage;
    }

    public static String getBaseUrl() {
        return SiteConfig.resolveBaseUrl();
    }
}
