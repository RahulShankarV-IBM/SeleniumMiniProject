package com.propfind.hooks;

import com.propfind.context.CompareTestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

/**
 * Cucumber hooks for the Compare feature.
 * Handles screenshot-on-failure and driver teardown per scenario.
 */
public class CompareHooks {

    private final CompareTestContext ctx;

    public CompareHooks(CompareTestContext ctx) {
        this.ctx = ctx;
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        // Nothing needed before each scenario — driver is created in CompareTestContext constructor
    }

    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            try {
                byte[] screenshot = ((org.openqa.selenium.TakesScreenshot) ctx.getDriver())
                        .getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Failure screenshot");
            } catch (Exception ignored) {
                // Driver may already be gone; skip screenshot silently
            }
        }
        ctx.getDriver().quit();
    }
}
