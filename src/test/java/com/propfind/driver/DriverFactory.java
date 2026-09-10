package com.propfind.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

/**
 * Central factory for creating a configured WebDriver instance.
 * Both BaseTestClass (plain TestNG) and the Cucumber TestContext classes
 * delegate here so Chrome options are never duplicated.
 *
 * <h3>Headless mode</h3>
 * Resolution order (first match wins):
 * <ol>
 *   <li>System property {@code -Dheadless=true|false} — highest priority, works locally and in CI</li>
 *   <li>{@code GITHUB_ACTIONS} environment variable present → headless on</li>
 *   <li>Default → headed (maximised window)</li>
 * </ol>
 * Examples:
 * <pre>
 *   mvn test                        # headed, local default
 *   mvn test -Dheadless=true        # headless, any environment
 *   mvn test -Dheadless=false       # forced headed even on CI
 * </pre>
 */
public class DriverFactory {

    private DriverFactory() {}

    /** Returns a configured ChromeDriver for local file:// testing. */
    public static WebDriver createChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--allow-file-access-from-files");

        if (isHeadless()) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");
        } else {
            options.addArguments("--start-maximized");
        }

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0)); // rely on explicit waits only
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        return driver;
    }

    /**
     * Returns true when headless mode should be used.
     * Checks the {@code headless} system property first, then falls back to
     * the {@code GITHUB_ACTIONS} environment variable.
     */
    private static boolean isHeadless() {
        String prop = System.getProperty("headless");
        if (prop != null) {
            return Boolean.parseBoolean(prop);
        }
        return System.getenv("GITHUB_ACTIONS") != null;
    }
}
