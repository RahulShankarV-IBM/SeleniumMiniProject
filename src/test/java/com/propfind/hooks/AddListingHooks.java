package com.propfind.hooks;

import com.propfind.context.AddListingTestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

/**
 * Cucumber hooks for the Add Listing feature (TC61–TC65).
 * Handles screenshot-on-failure and driver teardown per scenario.
 */
public class AddListingHooks {

    private final AddListingTestContext ctx;

    public AddListingHooks(AddListingTestContext ctx) {
        this.ctx = ctx;
    }

    @Before("@US13")
    public void beforeScenario(Scenario scenario) {
        // Nothing needed before each AddListing scenario — driver initialises lazily on first step.
    }

    @After("@US13")
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
