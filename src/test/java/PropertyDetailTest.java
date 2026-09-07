import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * TestNG runner for the Property Detail feature (TC21–TC30, US05 + US06).
 *
 * Cucumber picks up:
 *   features → src/test/resources/features/PropertyDetail.feature
 *   glue     → PropertyDetailSteps, PropertyDetailHooks, PropertyDetailTestContext
 *
 * Reports:
 *   - Cucumber HTML → target/cucumber-reports/propertydetail/index.html
 *   - Cucumber JSON → target/cucumber-reports/propertydetail/cucumber.json
 *   - Extent Reports → via ExtentReportListener registered in test.xml
 */
@CucumberOptions(
    features   = "src/test/resources/features/PropertyDetail.feature",
    glue       = "",
    plugin     = {
        "pretty",
        "html:target/cucumber-reports/propertydetail/index.html",
        "json:target/cucumber-reports/propertydetail/cucumber.json"
    },
    monochrome = true
)
public class PropertyDetailTest extends AbstractTestNGCucumberTests {
}
