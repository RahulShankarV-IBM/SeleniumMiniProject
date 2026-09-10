package com.propfind.context;

import com.propfind.config.SiteConfig;
import com.propfind.driver.DriverFactory;
import com.propfind.pages.MovingAssistantPage;
import org.openqa.selenium.WebDriver;

/**
 * Cucumber PicoContainer context — one instance per scenario.
 * Holds the WebDriver and MovingAssistantPage so they can be injected into
 * both MovingAssistantSteps and MovingAssistantHooks without static state.
 *
 * The driver is created lazily on first access so that dry-runs or
 * tag-filtered scenarios that skip all steps never open a browser.
 */
public class MovingAssistantTestContext {

    private WebDriver driver;
    private MovingAssistantPage movingAssistantPage;

    /** Lazily initialises the driver and page on first call. */
    public WebDriver getDriver() {
        if (driver == null) {
            driver               = DriverFactory.createChromeDriver();
            movingAssistantPage  = new MovingAssistantPage(driver);
        }
        return driver;
    }

    public MovingAssistantPage getMovingAssistantPage() {
        getDriver();
        return movingAssistantPage;
    }

    public static String getBaseUrl() {
        return SiteConfig.resolveBaseUrl();
    }
}
