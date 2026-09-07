import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

/**
 * Cucumber hooks for PropertyDetail scenarios.
 * Attaches a screenshot on failure and always quits the driver.
 */
public class PropertyDetailHooks {

    private final PropertyDetailTestContext ctx;

    public PropertyDetailHooks(PropertyDetailTestContext ctx) {
        this.ctx = ctx;
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        // Driver is created in PropertyDetailTestContext constructor
    }

    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            try {
                byte[] screenshot = ((org.openqa.selenium.TakesScreenshot) ctx.getDriver())
                        .getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Failure screenshot");
            } catch (Exception ignored) { }
        }
        ctx.getDriver().quit();
    }
}
