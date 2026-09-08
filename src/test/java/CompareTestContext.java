import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.time.Duration;

/**
 * Cucumber PicoContainer context — one instance per scenario.
 * Holds the WebDriver and ComparePage so they can be injected into
 * both CompareSteps and CompareHooks without static state.
 */
public class CompareTestContext {

    private final WebDriver driver;
    private final ComparePage comparePage;
    private static String baseUrl;

    public CompareTestContext() {
        if (baseUrl == null || baseUrl.isBlank()) {
            File siteRoot = new File("src/test/resources/TargetWebsite - Copy");
            baseUrl = siteRoot.toURI().toString();
        }

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized", "--allow-file-access-from-files");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        comparePage = new ComparePage(driver);
    }

    public WebDriver getDriver()          { return driver; }
    public ComparePage getComparePage()   { return comparePage; }
    public static String getBaseUrl()     { return baseUrl; }
}
