import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object for login.html
 * Contains all locators and user-action methods for the Login / Register page.
 * No assertions live here — all assertions belong in step definitions.
 */
public class LoginPage {

    // ── Locators ─────────────────────────────────────────────────────────────
    private static final By TAB_LOGIN       = By.id("tab-login");
    private static final By TAB_REGISTER    = By.id("tab-register");

    private static final By FORM_LOGIN      = By.id("form-login");
    private static final By LOGIN_ID        = By.id("login-id");
    private static final By LOGIN_PWD       = By.id("login-pwd");
    private static final By BTN_LOGIN       = By.id("btn-login");
    private static final By LOGIN_ALERT     = By.id("login-alert");

    private static final By FORM_REGISTER   = By.id("form-register");
    private static final By REG_NAME        = By.id("reg-name");
    private static final By REG_ID          = By.id("reg-id");
    private static final By REG_PWD         = By.id("reg-pwd");
    private static final By REG_CPWD        = By.id("reg-cpwd");
    private static final By BTN_REGISTER    = By.id("btn-register");
    private static final By REG_ALERT       = By.id("reg-alert");

    // ── State ─────────────────────────────────────────────────────────────────
    private final WebDriver driver;
    private final WebDriverWait wait;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    public void navigateTo(String baseUrl) {
        driver.get(baseUrl + "login.html");
        wait.until(ExpectedConditions.visibilityOfElementLocated(TAB_LOGIN));
    }

    // ── Tab switching ─────────────────────────────────────────────────────────

    public void switchToRegisterTab() {
        wait.until(ExpectedConditions.elementToBeClickable(TAB_REGISTER)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(FORM_REGISTER));
    }

    public void switchToLoginTab() {
        wait.until(ExpectedConditions.elementToBeClickable(TAB_LOGIN)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(FORM_LOGIN));
    }

    // ── Login actions ─────────────────────────────────────────────────────────

    public void enterLoginUsername(String username) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(LOGIN_ID));
        field.clear();
        field.sendKeys(username);
    }

    public void enterLoginPassword(String password) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(LOGIN_PWD));
        field.clear();
        field.sendKeys(password);
    }

    public void submitLoginForm() {
        wait.until(ExpectedConditions.elementToBeClickable(BTN_LOGIN)).click();
    }

    // ── Register actions ──────────────────────────────────────────────────────

    public void enterRegisterName(String name) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(REG_NAME));
        field.clear();
        field.sendKeys(name);
    }

    public void enterRegisterUsername(String username) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(REG_ID));
        field.clear();
        field.sendKeys(username);
    }

    public void enterRegisterPassword(String password) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(REG_PWD));
        field.clear();
        field.sendKeys(password);
    }

    public void enterConfirmPassword(String password) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(REG_CPWD));
        field.clear();
        field.sendKeys(password);
    }

    public void submitRegistrationForm() {
        wait.until(ExpectedConditions.elementToBeClickable(BTN_REGISTER)).click();
    }

    public void submitRegistrationFormEmpty() {
        // Switch to register tab then click submit without filling fields
        switchToRegisterTab();
        wait.until(ExpectedConditions.elementToBeClickable(BTN_REGISTER)).click();
    }

    // ── Pre-seed a user via localStorage (bypasses UI for setup steps) ────────

    /**
     * Seeds a user directly into localStorage so it exists before the test
     * exercises the UI — avoids relying on a prior UI registration flow.
     * Shape matches Auth.register() output from properties.js.
     */
    public void seedUser(String username, String password) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String userJson = String.format(
            "[{\"id\":99999,\"name\":\"Test User\",\"email\":\"\",\"username\":\"%s\"," +
            "\"password\":\"%s\",\"favorites\":[],\"recentlyViewed\":[],\"appointments\":[]}]",
            username, password);
        js.executeScript("localStorage.setItem('prop_users', arguments[0]);", userJson);
    }

    // ── Query methods (for assertions in step definitions) ────────────────────

    public boolean isLoginAlertVisible() {
        try {
            WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(LOGIN_ALERT));
            return !alert.getText().isBlank();
        } catch (Exception e) {
            return false;
        }
    }

    public String getLoginAlertText() {
        return driver.findElement(LOGIN_ALERT).getText().trim();
    }

    public boolean isRegAlertVisible() {
        try {
            WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(REG_ALERT));
            return !alert.getText().isBlank();
        } catch (Exception e) {
            return false;
        }
    }

    public String getRegAlertText() {
        return driver.findElement(REG_ALERT).getText().trim();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public boolean isRegisterFormVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(FORM_REGISTER)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isFieldInvalid(By locator) {
        // HTML5 constraint validation sets :invalid pseudo-class; check via JS
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement el = driver.findElement(locator);
        return !(Boolean) js.executeScript("return arguments[0].validity.valid;", el);
    }

    public boolean areRequiredFieldsInvalid() {
        return isFieldInvalid(REG_NAME) || isFieldInvalid(REG_ID)
                || isFieldInvalid(REG_PWD) || isFieldInvalid(REG_CPWD);
    }
}
