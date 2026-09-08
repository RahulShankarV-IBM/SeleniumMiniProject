import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.time.Duration;

/**
 * Cucumber PicoContainer context — one instance per scenario.
 * Holds the WebDriver and BudgetPlannerPage so they can be injected into
 * both BudgetPlannerSteps and BudgetPlannerHooks without static state.
 */
public class BudgetPlannerTestContext {

    private final WebDriver driver;
    private final BudgetPlannerPage budgetPlannerPage;
    private static String baseUrl;

    public BudgetPlannerTestContext() {
        if (baseUrl == null || baseUrl.isBlank()) {
            File siteRoot = new File("src/test/resources/TargetWebsite - Copy");
            baseUrl = siteRoot.toURI().toString();
        }

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized", "--allow-file-access-from-files");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        budgetPlannerPage = new BudgetPlannerPage(driver);
    }

    public WebDriver getDriver()                      { return driver; }
    public BudgetPlannerPage getBudgetPlannerPage()   { return budgetPlannerPage; }
    public static String getBaseUrl()                 { return baseUrl; }
}
