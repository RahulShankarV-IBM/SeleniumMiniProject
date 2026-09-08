package com.propfind.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * TestNG runner for the Map View feature (TC16–TC20).
 *
 * Cucumber picks up:
 *   features  → src/test/resources/features/MapView.feature
 *   glue      → com.propfind (all sub-packages: steps, hooks, context)
 *
 * Reports:
 *   - Cucumber HTML  → target/cucumber-reports/mapview/index.html
 *   - Cucumber JSON  → target/cucumber-reports/mapview/cucumber.json  (CI-friendly)
 *   - Extent Reports → picked up automatically via ExtentReportListener in test.xml
 */
@CucumberOptions(
    features = "src/test/resources/features/MapView.feature",
    glue     = "com.propfind",
    plugin   = {
        "pretty",
        "html:target/cucumber-reports/mapview/index.html",
        "json:target/cucumber-reports/mapview/cucumber.json"
    },
    monochrome = true
)
public class MapViewTest extends AbstractTestNGCucumberTests {
}
