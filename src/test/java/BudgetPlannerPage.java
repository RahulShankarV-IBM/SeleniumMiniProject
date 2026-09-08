import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object for budget-planner.html (US12 – TC56–TC60).
 * No assertions live here — all assertions belong in step definitions.
 */
public class BudgetPlannerPage {

    // ── Locators ──────────────────────────────────────────────────────────────
    private static final By INP_SALARY      = By.id("inp-salary");
    private static final By INP_RENT        = By.id("inp-rent");
    private static final By INP_MAINTENANCE = By.id("inp-maintenance");
    private static final By INP_ELECTRICITY = By.id("inp-electricity");
    private static final By INP_FOOD        = By.id("inp-food");
    private static final By INP_TRANSPORT   = By.id("inp-transport");
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

    public void enterSalary(String salary) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(INP_SALARY));
        field.clear();
        field.sendKeys(salary);
    }
    public void enterInvalidValue(String value) {
    JavascriptExecutor js = (JavascriptExecutor) driver;

    WebElement field = wait.until(
            ExpectedConditions.visibilityOfElementLocated(INP_SALARY)
    );

    // Temporarily change the input to text so that "abc" can be entered.
    js.executeScript(
            "arguments[0].type = 'text';" +
            "arguments[0].value = arguments[1];" +
            "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));",
            field, value
    );
}
    /**
     * Sets the rent slider value via JavaScript (range inputs don't support sendKeys reliably).
     * Also fires the oninput event so the JS handler updates the display and recalculates.
     */
    public void setRent(String rentValue) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement slider = wait.until(ExpectedConditions.visibilityOfElementLocated(INP_RENT));
        js.executeScript("arguments[0].value = arguments[1];", slider, rentValue);
        js.executeScript(
                "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", slider);
    }

    public void enterMaintenance(String value) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(INP_MAINTENANCE));
        field.clear();
        field.sendKeys(value);
    }

    public void enterElectricity(String value) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(INP_ELECTRICITY));
        field.clear();
        field.sendKeys(value);
    }

    public void enterFood(String value) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(INP_FOOD));
        field.clear();
        field.sendKeys(value);
    }

    public void enterTransport(String value) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(INP_TRANSPORT));
        field.clear();
        field.sendKeys(value);
    }

    public void clickCalculate() {
        wait.until(ExpectedConditions.elementToBeClickable(BTN_CALCULATE)).click();
        // Wait for the results panel to be updated with actual content
        wait.until(ExpectedConditions.visibilityOfElementLocated(RESULTS_PANEL));
    }

    // ── Query methods ─────────────────────────────────────────────────────────

    /** Returns true if the results panel contains the monthly total expenses card. */
    public boolean isResultsPanelPopulated() {
        try {
            WebElement panel = driver.findElement(RESULTS_PANEL);
            return panel.findElements(By.cssSelector(".result-card")).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns the affordability text from the results panel.
     * Looks for "Affordable" or "High" inside the second result-card.
     */
    public String getAffordabilityText() {
        try {
            WebElement panel = driver.findElement(RESULTS_PANEL);
            // The second result-card contains the rent affordability status
            WebElement affordCard = panel.findElements(
                    By.cssSelector(".result-card")).get(1);
            return affordCard.findElement(By.cssSelector(".big-num")).getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    /** Returns true when the results panel contains a monthly savings section. */
    public boolean isSavingsSectionVisible() {
        try {
            WebElement panel = driver.findElement(RESULTS_PANEL);
            // Savings section is a div containing "Monthly Savings" text
            return panel.getText().contains("Monthly Savings");
        } catch (Exception e) {
            return false;
        }
    }

    /** Returns true when the results panel contains the affordable rent range text. */
    public boolean isAffordableRentRangeVisible() {
        try {
            WebElement panel = driver.findElement(RESULTS_PANEL);
            // The affordability card sub-text includes "Ideal max rent"
            return panel.getText().contains("Ideal max rent");
        } catch (Exception e) {
            return false;
        }
    }

    /** Returns true when the results panel shows an invalid-input error alert. */
    public boolean isInvalidInputErrorVisible() {
        try {
            WebElement panel = driver.findElement(RESULTS_PANEL);
            return panel.findElements(By.cssSelector(".alert-error")).size() > 0
                    && panel.findElement(By.cssSelector(".alert-error")).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /** Returns true when the monthly cost breakdown section is present in the results panel. */
    public boolean isCostBreakdownVisible() {
        try {
            WebElement panel = driver.findElement(RESULTS_PANEL);
            // The breakdown planner-card contains the heading "Monthly Cost Breakdown"
            return panel.getText().contains("Monthly Cost Breakdown");
        } catch (Exception e) {
            return false;
        }
    }
}
