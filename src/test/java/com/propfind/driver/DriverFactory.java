package com.propfind.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

/**
 * Central factory for creating a configured WebDriver instance.
 * Both BaseTestClass (plain TestNG) and the Cucumber TestContext classes
 * delegate here so Chrome options are never duplicated.
 */
public class DriverFactory {

    private DriverFactory() {}

    /** Returns a maximised ChromeDriver with settings required for local file:// testing. */
    public static WebDriver createChromeDriver() {
        ChromeOptions options = new ChromeOptions();

        if (System.getenv("GITHUB_ACTIONS") != null) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");
        } else {
            options.addArguments("--start-maximized");
        }

        options.addArguments("--allow-file-access-from-files");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0)); // rely on explicit waits only
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        return driver;
    }
}
