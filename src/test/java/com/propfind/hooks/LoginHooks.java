package com.propfind.hooks;

import com.propfind.context.LoginTestContext;
import com.propfind.listeners.ExtentReportManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

/**
 * Cucumber hooks for the Login feature.
 * Handles screenshot-on-failure and driver teardown per scenario.
 * Pass/fail logging is handled globally by {@link CucumberExtentHooks}.
 */
public class LoginHooks {

    private final LoginTestContext ctx;

    public LoginHooks(LoginTestContext ctx) {
        this.ctx = ctx;
    }

    @Before("@US01")
    public void beforeScenario(Scenario scenario) {
        // Nothing needed before each Login scenario — driver initialises lazily on first step.
    }

    @After("@US01")
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            try {
                byte[] screenshot = ((org.openqa.selenium.TakesScreenshot) ctx.getDriver())
                        .getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Failure screenshot");
                String b64 = java.util.Base64.getEncoder().encodeToString(screenshot);
                ExtentReportManager.attachScreenshot(b64, "Failure screenshot");
            } catch (Exception ignored) {
                // Driver may already be gone; skip screenshot silently
            }
        }
        ctx.getDriver().quit();
    }
}
