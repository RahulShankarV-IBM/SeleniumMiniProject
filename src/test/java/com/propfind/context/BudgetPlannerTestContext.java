package com.propfind.context;

import com.propfind.driver.DriverFactory;
import com.propfind.pages.BudgetPlannerPage;
import org.openqa.selenium.WebDriver;

import java.io.File;

/**
 * Cucumber PicoContainer context — one instance per scenario.
 * Holds the WebDriver and BudgetPlannerPage so they can be injected into
 * both BudgetPlannerSteps and BudgetPlannerHooks without static state.
 */
public class BudgetPlannerTestContext {

    private final WebDriver driver;
    private final BudgetPlannerPage budgetPlannerPage;
    private static String baseUrl;

    public BudgetPlannerTestContext() {
        if (baseUrl == null || baseUrl.isBlank()) {
            File siteRoot = new File("propfind-website");
            baseUrl = siteRoot.toURI().toString();
        }
        driver            = DriverFactory.createChromeDriver();
        budgetPlannerPage = new BudgetPlannerPage(driver);
    }

    public WebDriver getDriver()                       { return driver; }
    public BudgetPlannerPage getBudgetPlannerPage()    { return budgetPlannerPage; }
    public static String getBaseUrl()                  { return baseUrl; }
}
