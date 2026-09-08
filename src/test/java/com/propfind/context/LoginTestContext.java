package com.propfind.context;

import com.propfind.driver.DriverFactory;
import com.propfind.pages.LoginPage;
import org.openqa.selenium.WebDriver;

import java.io.File;

/**
 * Cucumber PicoContainer context — one instance per scenario.
 * Holds the WebDriver and LoginPage so they can be injected into
 * both LoginSteps and LoginHooks without static state.
 */
public class LoginTestContext {

    private final WebDriver driver;
    private final LoginPage loginPage;
    private static String baseUrl;

    public LoginTestContext() {
        // Resolve baseUrl once from the bundled propfind-website directory at project root
        if (baseUrl == null || baseUrl.isBlank()) {
            File siteRoot = new File("propfind-website");
            baseUrl = siteRoot.toURI().toString();
        }

        driver    = DriverFactory.createChromeDriver();
        loginPage = new LoginPage(driver);
    }

    public WebDriver getDriver()       { return driver; }
    public LoginPage getLoginPage()    { return loginPage; }
    public static String getBaseUrl()  { return baseUrl; }
}
