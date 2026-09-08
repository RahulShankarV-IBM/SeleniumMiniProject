package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.io.File;

/**
 * BaseTest — shared WebDriver lifecycle for all TestNG test classes.
 *
 * Usage:
 *   Extend this class in each page-level test class.
 *   The baseUrl and headless flag can be overridden via testng.xml <parameter> entries.
 *   If no baseUrl is supplied, it resolves automatically to the TargetWebsite folder
 *   that ships inside this project at src/test/resources/TargetWebsite/ — so the
 *   default works on every machine without any manual path editing.
 *
 * testng.xml example (optional override):
 *   <parameter name="baseUrl"  value="file:///C:/custom/path/to/TargetWebsite/"/>
 *   <parameter name="headless" value="false"/>
 */
public class BaseTest {

    protected WebDriver driver;
    protected String baseUrl;

    /** Resolves to the TargetWebsite folder bundled in src/test/resources. */
    private static final String DEFAULT_BASE_URL =
            new File("src/test/resources/TargetWebsite").toURI().toString();

    @BeforeMethod
    @Parameters({"baseUrl", "headless"})
    public void setUp(
            @Optional("") String baseUrl,
            @Optional("false") String headless) {

        // Use the auto-resolved local path when no override is provided in testng.xml
        String resolvedUrl = (baseUrl == null || baseUrl.isBlank())
                ? DEFAULT_BASE_URL
                : baseUrl;
        this.baseUrl = resolvedUrl.endsWith("/") ? resolvedUrl : resolvedUrl + "/";

        ChromeOptions options = new ChromeOptions();
        if ("true".equalsIgnoreCase(headless)) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--start-maximized");
        options.addArguments("--disable-search-engine-choice-screen");

        driver = new ChromeDriver(options);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
