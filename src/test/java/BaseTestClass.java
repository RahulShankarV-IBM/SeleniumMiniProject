import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.io.File;
import java.time.Duration;

public class BaseTestClass {

    protected WebDriver driver;
    protected String baseUrl;

    /**
     * Receives the optional baseUrl parameter from test.xml.
     * Falls back to the local TargetWebsite copy bundled in test resources.
     */
    @BeforeMethod
    @Parameters("baseUrl")
    public void setUp(@Optional("") String baseUrl) {
        if (baseUrl == null || baseUrl.isBlank()) {
            // Resolve the bundled HTML site from the classpath-accessible resources directory
            File siteRoot = new File("src/test/resources/TargetWebsite - Copy");
            this.baseUrl = siteRoot.toURI().toString();
        } else {
            this.baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
        }

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--allow-file-access-from-files");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0)); // rely on explicit waits only
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /** Convenience helper: build a full file URL for a given HTML page name. */
    protected String pageUrl(String htmlFileName) {
        return baseUrl + htmlFileName;
    }

    /**
     * Captures a screenshot as a Base64-encoded PNG string.
     * Returns null if the driver is not available or does not support screenshots.
     * Used by ExtentReportListener; can also be called directly in test methods.
     */
    protected String captureScreenshotAsBase64() {
        if (driver instanceof org.openqa.selenium.TakesScreenshot ts) {
            return java.util.Base64.getEncoder().encodeToString(
                    ts.getScreenshotAs(org.openqa.selenium.OutputType.BYTES));
        }
        return null;
    }
}
