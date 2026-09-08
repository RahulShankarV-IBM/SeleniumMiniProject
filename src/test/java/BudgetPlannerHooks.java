import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

/**
 * Cucumber hooks for the Budget Planner feature (TC56–TC60, US12).
 * Handles screenshot-on-failure and driver teardown per scenario.
 */
public class BudgetPlannerHooks {

    private final BudgetPlannerTestContext ctx;

    public BudgetPlannerHooks(BudgetPlannerTestContext ctx) {
        this.ctx = ctx;
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        // Driver is created in BudgetPlannerTestContext constructor — nothing additional needed.
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
