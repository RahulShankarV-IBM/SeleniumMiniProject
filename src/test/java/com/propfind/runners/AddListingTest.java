package com.propfind.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * TestNG runner for the Add Listing feature (TC61–TC65).
 *
 * Cucumber picks up:
 *   features  → src/test/resources/features/AddListing.feature
 *   glue      → com.propfind (all sub-packages: steps, hooks, context)
 *
 * Reports:
 *   - Cucumber HTML  → target/cucumber-reports/addlisting/index.html
 *   - Cucumber JSON  → target/cucumber-reports/addlisting/cucumber.json
 *   - Extent Reports → picked up automatically via ExtentReportListener in test.xml
 */
@CucumberOptions(
    features   = "src/test/resources/features/AddListing.feature",
    glue       = "com.propfind",
    plugin     = {
        "pretty",
        "html:target/cucumber-reports/addlisting/index.html",
        "json:target/cucumber-reports/addlisting/cucumber.json"
    },
    monochrome = true
)
public class AddListingTest extends AbstractTestNGCucumberTests {
}
