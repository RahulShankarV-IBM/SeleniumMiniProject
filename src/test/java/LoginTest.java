import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * TestNG runner for the Login / Register feature (TC01–TC05).
 *
 * Cucumber picks up:
 *   features  → src/test/resources/features/Login.feature
 *   glue      → LoginSteps, LoginHooks, LoginTestContext (all in default package)
 *
 * Reports:
 *   - Cucumber HTML  → target/cucumber-reports/login/index.html
 *   - Cucumber JSON  → target/cucumber-reports/login/cucumber.json  (CI-friendly)
 *   - Extent Reports → picked up automatically via ExtentReportListener in test.xml
 */
@CucumberOptions(
    features = "src/test/resources/features/Login.feature",
    glue     = "",          // default package — all step/hook classes are here
    plugin   = {
        "pretty",
        "html:target/cucumber-reports/login/index.html",
        "json:target/cucumber-reports/login/cucumber.json"
    },
    monochrome = true
)
public class LoginTest extends AbstractTestNGCucumberTests {
}
