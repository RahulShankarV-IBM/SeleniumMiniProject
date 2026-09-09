package com.propfind.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object for budget-planner.html
 * Contains all locators and user-action methods for the Budget Planner page.
 * No assertions live here — all assertions belong in step definitions.
 */
public class BudgetPlannerPage {

    // ── Locators ─────────────────────────────────────────────────────────────
    private static final By INP_SALARY      = By.id("inp-salary");
    private static final By INP_RENT        = By.id("inp-rent");
    private static final By INP_MAINTENANCE = By.id("inp-maintenance");
    private static final By INP_ELECTRICITY = By.id("inp-electricity");
    private static final By INP_INTERNET    = By.id("inp-internet");
    private static final By INP_PARKING     = By.id("inp-parking");
    private static final By INP_FOOD        = By.id("inp-food");
    private static final By INP_TRANSPORT   = By.id("inp-transport");
    private static final By INP_OTHER       = By.id("inp-other");
    private static final By INP_DEPOSIT     = By.id("inp-deposit");
    private static final By INP_MOVING      = By.id("inp-moving");
    private static final By BTN_CALCULATE   = By.id("btn-calculate");
    private static final By RESULTS_PANEL   = By.id("results-panel");

    // ── State ─────────────────────────────────────────────────────────────────
    private final WebDriver driver;
    private final WebDriverWait wait;

    public BudgetPlannerPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    public void navigateTo(String baseUrl) {
        driver.get(baseUrl + "budget-planner.html");
        wait.until(ExpectedConditions.visibilityOfElementLocated(BTN_CALCULATE));
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    public void enterSalary(int salary) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(INP_SALARY));
        field.clear();
        field.sendKeys(String.valueOf(salary));
    }

    /**
     * Sets the rent slider value via JavaScript, since sendKeys on range inputs is unreliable.
     */
    public void enterRent(int rent) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement slider = wait.until(ExpectedConditions.visibilityOfElementLocated(INP_RENT));
        js.executeScript("arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('input'));",
                slider, String.valueOf(rent));
    }

    public void enterMaintenance(int amount) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(INP_MAINTENANCE));
        field.clear();
        field.sendKeys(String.valueOf(amount));
    }

    public void enterElectricity(int amount) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(INP_ELECTRICITY));
        field.clear();
        field.sendKeys(String.valueOf(amount));
    }

    public void enterInternet(int amount) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(INP_INTERNET));
        field.clear();
        field.sendKeys(String.valueOf(amount));
    }

    public void enterFood(int amount) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(INP_FOOD));
        field.clear();
        field.sendKeys(String.valueOf(amount));
    }

    public void enterTransport(int amount) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(INP_TRANSPORT));
        field.clear();
        field.sendKeys(String.valueOf(amount));
    }

    /**
     * Enters an invalid (non-numeric) string value into the salary field via JavaScript,
     * bypassing the browser's type="number" constraint so the JS validation path is exercised.
     */
    public void enterInvalidSalaryValue(String value) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(INP_SALARY));
        js.executeScript("arguments[0].removeAttribute('type');", field);
        field.clear();
        field.sendKeys(value);
    }

    public void clickCalculate() {
        wait.until(ExpectedConditions.elementToBeClickable(BTN_CALCULATE)).click();
        // Wait for results panel to be updated (non-empty)
        wait.until(driver -> {
            WebElement panel = driver.findElement(RESULTS_PANEL);
            return !panel.getText().isBlank();
        });
    }

    // ── Query methods (for assertions in step definitions) ────────────────────

    /**
     * Returns true if the results panel is visible and contains text content.
     */
    public boolean isResultsPanelPopulated() {
        try {
            WebElement panel = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(RESULTS_PANEL));
            return !panel.getText().isBlank();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns the full text content of the results panel.
     */
    public String getResultsPanelText() {
        return driver.findElement(RESULTS_PANEL).getText().trim();
    }

    /**
     * Returns true if the results panel contains an affordability indicator
     * (either "Affordable" or "High").
     */
    public boolean isAffordabilityDisplayed() {
        String text = getResultsPanelText();
        return text.contains("Affordable") || text.contains("High");
    }

    /**
     * Returns true if the results panel contains a validation error alert
     * (signalled by the word "Invalid" in the rendered HTML).
     */
    public boolean isValidationErrorDisplayed() {
        String text = getResultsPanelText();
        return text.contains("Invalid");
    }

    /**
     * Returns true if the results panel contains the monthly cost breakdown section.
     */
    public boolean isCostBreakdownDisplayed() {
        String text = getResultsPanelText();
        return text.contains("Monthly Cost Breakdown") || text.contains("Monthly Total");
    }

    /**
     * Returns true if the results panel contains savings information.
     */
    public boolean isSavingsDisplayed() {
        String text = getResultsPanelText();
        return text.contains("Monthly Savings") || text.contains("surplus") || text.contains("deficit");
    }

    /**
     * Returns true if the results panel contains affordable rent guidance
     * (from the 30% rule).
     */
    public boolean isAffordableRentDisplayed() {
        String text = getResultsPanelText();
        return text.contains("30%") || text.contains("Ideal max rent");
    }
}
