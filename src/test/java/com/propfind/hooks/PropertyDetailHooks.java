package com.propfind.hooks;

import com.propfind.context.PropertyDetailTestContext;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;

/**
 * Cucumber hooks for the Property Detail feature (TC21–TC30, TC41–TC50, TC66–TC70).
 * Handles screenshot-on-failure and driver teardown per scenario.
 */
public class PropertyDetailHooks {

    private final PropertyDetailTestContext ctx;

    public PropertyDetailHooks(PropertyDetailTestContext ctx) {
        this.ctx = ctx;
    }

    @After("@US05 or @US06 or @US09 or @US10 or @US14")
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
