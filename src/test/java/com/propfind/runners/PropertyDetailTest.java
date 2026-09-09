package com.propfind.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * TestNG runner for the Property Detail feature (TC21–TC30, TC41–TC50, TC66–TC70).
 *
 * Cucumber picks up:
 *   features → src/test/resources/features/PropertyDetail.feature
 *   glue     → com.propfind (all sub-packages: steps, hooks, context)
 *
 * Reports:
 *   - Cucumber HTML  → target/cucumber-reports/property-detail/index.html
 *   - Cucumber JSON  → target/cucumber-reports/property-detail/cucumber.json  (CI-friendly)
 *   - Extent Reports → picked up automatically via ExtentReportListener in test.xml
 */
@CucumberOptions(
    features   = "src/test/resources/features/PropertyDetail.feature",
    glue       = "com.propfind",
    plugin     = {
        "pretty",
        "html:target/cucumber-reports/property-detail/index.html",
        "json:target/cucumber-reports/property-detail/cucumber.json"
    },
    monochrome = true
)
public class PropertyDetailTest extends AbstractTestNGCucumberTests {
}
