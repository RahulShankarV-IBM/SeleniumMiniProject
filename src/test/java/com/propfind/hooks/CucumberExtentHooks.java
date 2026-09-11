package com.propfind.hooks;

import com.propfind.listeners.ExtentReportManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import java.util.Collection;

/**
 * Global Cucumber hooks — run for <em>every</em> scenario regardless of tags.
 *
 * <h3>Why this class exists</h3>
 * When Cucumber runs via {@code AbstractTestNGCucumberTests} each scenario is
 * reported to TestNG as a method named {@code runScenario}.  The
 * {@link com.propfind.listeners.ExtentReportListener} therefore has no access
 * to the real scenario name or its tags.
 *
 * This class bridges that gap: the {@code @Before} hook creates a named,
 * tagged {@link com.aventstack.extentreports.ExtentTest} node for the scenario
 * <em>before</em> any steps run, and the {@code @After} hook logs the final
 * pass/fail result once all steps have finished.
 *
 * The per-feature Hooks classes (e.g. {@link LoginHooks}) continue to handle
 * driver teardown and screenshot-on-failure — they call
 * {@link ExtentReportManager#attachScreenshot} so the screenshot is embedded
 * in the correct node that this class created.
 *
 * <h3>Name format</h3>
 * {@code TC01 – Register with valid details}  (first @TCxx tag + scenario name)
 * If no @TCxx tag is present the scenario name alone is used.
 */
public class CucumberExtentHooks {

    /**
     * Order -1 ensures this runs <em>before</em> all feature-specific
     * {@code @Before} hooks (which default to order 0).
     */
    @Before(order = -1)
    public void beforeScenario(Scenario scenario) {
        // Build a display name:  "TC01 – Register with valid details"
        String tcTag  = extractTcTag(scenario.getSourceTagNames());
        String name   = tcTag.isEmpty()
                ? scenario.getName()
                : tcTag + " \u2013 " + scenario.getName();   // en-dash

        // Collect all tags as categories (strips the leading @)
        String[] categories = scenario.getSourceTagNames().stream()
                .map(t -> t.startsWith("@") ? t.substring(1) : t)
                .toArray(String[]::new);

        // Ensure the manager is initialised even if the TestNG listener hasn't fired yet.
        ExtentReportManager.init();
        ExtentReportManager.startTest(name, scenario.getId(), categories);
    }

    /**
     * For {@code @After}, Cucumber runs <em>higher</em> order numbers first.
     * Order -1 (lower than the default 0) therefore runs <em>last</em>, after
     * all feature-specific hooks have already attached screenshots.
     */
    @After(order = -1)
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            ExtentReportManager.failTest("Scenario failed: " + scenario.getName());
        } else {
            ExtentReportManager.passTest("Scenario passed");
        }
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    /**
     * Finds the first tag matching {@code @TCxx} (e.g. "@TC01") and returns it
     * without the leading {@code @}.  Returns an empty string if none is found.
     */
    private static String extractTcTag(Collection<String> tags) {
        return tags.stream()
                .filter(t -> t.matches("@TC\\d+"))
                .map(t -> t.substring(1))      // strip "@"
                .findFirst()
                .orElse("");
    }
}
