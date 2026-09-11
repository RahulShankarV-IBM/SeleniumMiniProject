package com.propfind.hooks;

import com.propfind.context.BudgetPlannerTestContext;
import com.propfind.listeners.ExtentReportManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

/**
 * Cucumber hooks for the Budget Planner feature.
 * Handles screenshot-on-failure and driver teardown per scenario.
 * Pass/fail logging is handled globally by {@link CucumberExtentHooks}.
 */
public class BudgetPlannerHooks {

    private final BudgetPlannerTestContext ctx;

    public BudgetPlannerHooks(BudgetPlannerTestContext ctx) {
        this.ctx = ctx;
    }

    @Before("@US12")
    public void beforeScenario(Scenario scenario) {
        // Nothing needed before each BudgetPlanner scenario — driver initialises lazily on first step.
    }

    @After("@US12")
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
