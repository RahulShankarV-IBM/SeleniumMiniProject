package com.propfind.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * TestNG runner for the Moving Assistant feature (TC71–TC75).
 *
 * Cucumber picks up:
 *   features  → src/test/resources/features/MovingAssistant.feature
 *   glue      → com.propfind (all sub-packages: steps, hooks, context)
 *
 * Reports:
 *   - Cucumber HTML  → target/cucumber-reports/movingassistant/index.html
 *   - Cucumber JSON  → target/cucumber-reports/movingassistant/cucumber.json
 *   - Extent Reports → picked up automatically via ExtentReportListener in test.xml
 */
@CucumberOptions(
    features   = "src/test/resources/features/MovingAssistant.feature",
    glue       = "com.propfind",
    plugin     = {
        "pretty",
        "html:target/cucumber-reports/movingassistant/index.html",
        "json:target/cucumber-reports/movingassistant/cucumber.json"
    },
    monochrome = true
)
public class MovingAssistantTest extends AbstractTestNGCucumberTests {
}
