import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.time.Duration;

/**
 * Cucumber PicoContainer context for PropertyDetail scenarios.
 * One instance per scenario — creates the WebDriver and PropertyDetailPage.
 */
public class PropertyDetailTestContext {

    private final WebDriver driver;
    private final PropertyDetailPage page;
    private static String baseUrl;

    public PropertyDetailTestContext() {
        if (baseUrl == null || baseUrl.isBlank()) {
            File siteRoot = new File("src/test/resources/TargetWebsite - Copy");
            baseUrl = siteRoot.toURI().toString();
        }

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--allow-file-access-from-files");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        page = new PropertyDetailPage(driver);
    }

    public WebDriver getDriver()                    { return driver; }
    public PropertyDetailPage getPropertyDetailPage() { return page; }
    public static String getBaseUrl()               { return baseUrl; }
}
