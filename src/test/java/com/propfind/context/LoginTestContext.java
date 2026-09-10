package com.propfind.context;

import com.propfind.config.SiteConfig;
import com.propfind.driver.DriverFactory;
import com.propfind.pages.LoginPage;
import org.openqa.selenium.WebDriver;

/**
 * Cucumber PicoContainer context — one instance per scenario.
 * Holds the WebDriver and LoginPage so they can be injected into
 * both LoginSteps and LoginHooks without static state.
 *
 * The driver is created lazily on first access so that dry-runs or
 * tag-filtered scenarios that skip all steps never open a browser.
 */
public class LoginTestContext {

    private WebDriver driver;
    private LoginPage loginPage;

    /** Lazily initialises the driver and page on first call. */
    public WebDriver getDriver() {
        if (driver == null) {
            driver    = DriverFactory.createChromeDriver();
            loginPage = new LoginPage(driver);
        }
        return driver;
    }

    public LoginPage getLoginPage() {
        getDriver(); // ensure initialised
        return loginPage;
    }

    public static String getBaseUrl() {
        return SiteConfig.resolveBaseUrl();
    }
}
