package com.propfind.context;

import com.propfind.driver.DriverFactory;
import com.propfind.pages.MapViewPage;
import org.openqa.selenium.WebDriver;

import java.io.File;

/**
 * Cucumber PicoContainer context — one instance per scenario.
 * Holds the WebDriver and MapViewPage so they can be injected into
 * both MapViewSteps and MapViewHooks without static state.
 */
public class MapViewTestContext {

    private final WebDriver driver;
    private final MapViewPage mapViewPage;
    private static String baseUrl;

    public MapViewTestContext() {
        if (baseUrl == null || baseUrl.isBlank()) {
            File siteRoot = new File("propfind-website");
            baseUrl = siteRoot.toURI().toString();
        }
        driver      = DriverFactory.createChromeDriver();
        mapViewPage = new MapViewPage(driver);
    }

    public WebDriver getDriver()          { return driver; }
    public MapViewPage getMapViewPage()   { return mapViewPage; }
    public static String getBaseUrl()     { return baseUrl; }
}
