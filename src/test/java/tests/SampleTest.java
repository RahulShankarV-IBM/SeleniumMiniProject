package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.Test;

import java.io.File;

public class SampleTest {
    @Test
    public void DropTest() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        WebDriver driver = new ChromeDriver(options);
        try {
            File website = new File("src/test/resources/TargetWebsite/index.html");
            driver.get(website.toURI().toString());
        } finally {
            driver.quit();
        }
    }
}
