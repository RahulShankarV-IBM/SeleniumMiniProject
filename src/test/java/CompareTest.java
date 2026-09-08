import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * TestNG runner for the Compare Properties feature (TC36–TC40, US08).
 *
 * Cucumber picks up:
 *   features  → src/test/resources/features/Compare.feature
 *   glue      → CompareSteps, CompareHooks, CompareTestContext (all in default package)
 *
 * Reports:
 *   - Cucumber HTML  → target/cucumber-reports/compare/index.html
 *   - Cucumber JSON  → target/cucumber-reports/compare/cucumber.json
 *   - Extent Reports → picked up automatically via ExtentReportListener in test.xml
 */
@CucumberOptions(
    features = "src/test/resources/features/Compare.feature",
    glue     = "",
    plugin   = {
        "pretty",
        "html:target/cucumber-reports/compare/index.html",
        "json:target/cucumber-reports/compare/cucumber.json"
    },
    monochrome = true
)
public class CompareTest extends AbstractTestNGCucumberTests {
}
