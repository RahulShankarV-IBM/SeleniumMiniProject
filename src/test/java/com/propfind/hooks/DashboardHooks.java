package com.propfind.hooks;

import com.propfind.context.DashboardTestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

/**
 * Cucumber hooks for the Dashboard feature.
 * Authenticates user before scenario (Auth-protected page) and
 * handles screenshot-on-failure and driver teardown per scenario.
 */
public class DashboardHooks {

    private final DashboardTestContext ctx;

    public DashboardHooks(DashboardTestContext ctx) {
        this.ctx = ctx;
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        // Authenticate into dashboard session prior to scenario execution
        ctx.getDashboardPage().loginAs(DashboardTestContext.getBaseUrl(), "demo", "demo123");
    }

    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            try {
                byte[] screenshot = ((org.openqa.selenium.TakesScreenshot) ctx.getDriver())
                        .getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Failure screenshot");
            } catch (Exception ignored) {
            }
        }
        ctx.getDriver().quit();
    }
}
