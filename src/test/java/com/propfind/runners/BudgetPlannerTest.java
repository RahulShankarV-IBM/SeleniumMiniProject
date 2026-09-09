package com.propfind.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * TestNG runner for the Budget Planner feature (TC56–TC60).
 *
 * Cucumber picks up:
 *   features  → src/test/resources/features/BudgetPlanner.feature
 *   glue      → com.propfind (all sub-packages: steps, hooks, context)
 *
 * Reports:
 *   - Cucumber HTML  → target/cucumber-reports/budgetplanner/index.html
 *   - Cucumber JSON  → target/cucumber-reports/budgetplanner/cucumber.json  (CI-friendly)
 *   - Extent Reports → picked up automatically via ExtentReportListener in test.xml
 */
@CucumberOptions(
    features = "src/test/resources/features/BudgetPlanner.feature",
    glue     = "com.propfind",
    plugin   = {
        "pretty",
        "html:target/cucumber-reports/budgetplanner/index.html",
        "json:target/cucumber-reports/budgetplanner/cucumber.json"
    },
    monochrome = true
)
public class BudgetPlannerTest extends AbstractTestNGCucumberTests {
}
