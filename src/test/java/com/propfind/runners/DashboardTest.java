package com.propfind.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * TestNG runner for the Dashboard feature (US07, US11 / TC31–TC35, TC51–TC55).
 *
 * Cucumber configuration:
 *   features  → src/test/resources/features/Dashboard.feature
 *   glue      → com.propfind
 *
 * Reports:
 *   - Cucumber HTML  → target/cucumber-reports/dashboard/index.html
 *   - Cucumber JSON  → target/cucumber-reports/dashboard/cucumber.json
 */
@CucumberOptions(
    features = "src/test/resources/features/Dashboard.feature",
    glue     = "com.propfind",
    plugin   = {
        "pretty",
        "html:target/cucumber-reports/dashboard/index.html",
        "json:target/cucumber-reports/dashboard/cucumber.json"
    },
    monochrome = true
)
public class DashboardTest extends AbstractTestNGCucumberTests {
}
