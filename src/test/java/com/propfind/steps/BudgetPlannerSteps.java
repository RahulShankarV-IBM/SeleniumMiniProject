package com.propfind.steps;

import com.propfind.context.BudgetPlannerTestContext;
import com.propfind.pages.BudgetPlannerPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

/**
 * Step definitions for BudgetPlanner.feature (TC56–TC60).
 * All Selenium interactions are delegated to BudgetPlannerPage — no driver calls here.
 * The shared BudgetPlannerPage instance is provided via BudgetPlannerTestContext.
 */
public class BudgetPlannerSteps {

    private final BudgetPlannerPage budgetPlannerPage;

    public BudgetPlannerSteps(BudgetPlannerTestContext ctx) {
        this.budgetPlannerPage = ctx.getBudgetPlannerPage();
    }

    // ── Background ────────────────────────────────────────────────────────────

    @Given("I am on the budget planner page")
    public void iAmOnTheBudgetPlannerPage() {
        budgetPlannerPage.navigateTo(BudgetPlannerTestContext.getBaseUrl());
    }

    // ── When: input actions ───────────────────────────────────────────────────

    @When("I enter a monthly salary of {int}")
    public void iEnterAMonthlySalaryOf(int salary) {
        budgetPlannerPage.enterSalary(salary);
    }

    @When("I enter a monthly rent of {int}")
    public void iEnterAMonthlyRentOf(int rent) {
        budgetPlannerPage.enterRent(rent);
    }

    @And("I enter maintenance charges of {int}")
    public void iEnterMaintenanceChargesOf(int amount) {
        budgetPlannerPage.enterMaintenance(amount);
    }

    @And("I enter electricity charges of {int}")
    public void iEnterElectricityChargesOf(int amount) {
        budgetPlannerPage.enterElectricity(amount);
    }

    @And("I enter internet charges of {int}")
    public void iEnterInternetChargesOf(int amount) {
        budgetPlannerPage.enterInternet(amount);
    }

    @And("I enter food and grocery expenses of {int}")
    public void iEnterFoodAndGroceryExpensesOf(int amount) {
        budgetPlannerPage.enterFood(amount);
    }

    @And("I enter transport expenses of {int}")
    public void iEnterTransportExpensesOf(int amount) {
        budgetPlannerPage.enterTransport(amount);
    }

    @When("I enter an invalid non-numeric value in the salary field")
    public void iEnterAnInvalidNonNumericValueInTheSalaryField() {
        budgetPlannerPage.enterInvalidSalaryValue("abc!@#");
    }

    @And("I click the calculate budget button")
    public void iClickTheCalculateBudgetButton() {
        budgetPlannerPage.clickCalculate();
    }

    // ── Then: assertions ──────────────────────────────────────────────────────

    @Then("the results panel should display the total monthly expenses")
    public void theResultsPanelShouldDisplayTheTotalMonthlyExpenses() {
        Assert.assertTrue(budgetPlannerPage.isResultsPanelPopulated(),
                "Expected the results panel to be populated with budget calculation results");
        Assert.assertTrue(budgetPlannerPage.isCostBreakdownDisplayed(),
                "Expected the results panel to show the monthly total expenses");
    }

    @Then("the results panel should display rent affordability information")
    public void theResultsPanelShouldDisplayRentAffordabilityInformation() {
        Assert.assertTrue(budgetPlannerPage.isAffordabilityDisplayed(),
                "Expected the results panel to display rent affordability information (Affordable/High)");
    }

    @Then("the results panel should display estimated monthly savings")
    public void theResultsPanelShouldDisplayEstimatedMonthlySavings() {
        Assert.assertTrue(budgetPlannerPage.isSavingsDisplayed(),
                "Expected the results panel to display estimated monthly savings (surplus/deficit)");
    }

    @And("the results panel should display the recommended affordable rent")
    public void theResultsPanelShouldDisplayTheRecommendedAffordableRent() {
        Assert.assertTrue(budgetPlannerPage.isAffordableRentDisplayed(),
                "Expected the results panel to display the 30% rule affordable rent recommendation");
    }

    @Then("the results panel should display a validation error message")
    public void theResultsPanelShouldDisplayAValidationErrorMessage() {
        Assert.assertTrue(budgetPlannerPage.isValidationErrorDisplayed(),
                "Expected the results panel to display a validation error for invalid/non-numeric input");
    }

    @Then("the results panel should display the monthly cost breakdown summary")
    public void theResultsPanelShouldDisplayTheMonthlyCostBreakdownSummary() {
        Assert.assertTrue(budgetPlannerPage.isCostBreakdownDisplayed(),
                "Expected the results panel to display the monthly cost breakdown summary");
    }
}
