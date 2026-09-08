import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.time.Duration;

/**
 * Cucumber PicoContainer context — one instance per scenario.
 * Holds the WebDriver and LoginPage so they can be injected into
 * both LoginSteps and LoginHooks without static state.
 */
public class LoginTestContext {

    private final WebDriver driver;
    private final LoginPage loginPage;
    private static String baseUrl;

    public LoginTestContext() {
        // Resolve baseUrl once from the bundled resources directory
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

        loginPage = new LoginPage(driver);
    }

    public WebDriver getDriver()       { return driver; }
    public LoginPage getLoginPage()    { return loginPage; }
    public static String getBaseUrl()  { return baseUrl; }
}
