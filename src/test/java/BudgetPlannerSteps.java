import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

/**
 * Step definitions for BudgetPlanner.feature (TC56–TC60, US12).
 * All Selenium interactions are delegated to BudgetPlannerPage — no driver calls here.
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

    // ── Actions ───────────────────────────────────────────────────────────────

    @When("I enter a monthly salary of {string}")
    public void iEnterAMonthlySalaryOf(String salary) {
        budgetPlannerPage.enterSalary(salary);
    }

    @And("I set the rent to {string}")
    public void iSetTheRentTo(String rent) {
        budgetPlannerPage.setRent(rent);
    }
    @When("I enter an invalid value {string} in the salary field")
public void iEnterAnInvalidValueInTheSalaryField(String value) {
    budgetPlannerPage.enterInvalidValue(value);
}

    @And("I enter maintenance of {string}")
    public void iEnterMaintenanceOf(String value) {
        budgetPlannerPage.enterMaintenance(value);
    }

    @And("I enter electricity of {string}")
    public void iEnterElectricityOf(String value) {
        budgetPlannerPage.enterElectricity(value);
    }

    @And("I enter food expenses of {string}")
    public void iEnterFoodExpensesOf(String value) {
        budgetPlannerPage.enterFood(value);
    }

    @And("I enter transport expenses of {string}")
    public void iEnterTransportExpensesOf(String value) {
        budgetPlannerPage.enterTransport(value);
    }

    @And("I click the Calculate Budget button")
    public void iClickTheCalculateBudgetButton() {
        budgetPlannerPage.clickCalculate();
    }

    
    // ── Assertions ────────────────────────────────────────────────────────────

    @Then("the results panel should display the total monthly expenses")
    public void resultsPanelShouldDisplayTotalMonthlyExpenses() {
        Assert.assertTrue(budgetPlannerPage.isResultsPanelPopulated(),
                "Expected the results panel to show monthly expense cards after calculation");
    }

    @Then("the rent affordability result should be {string}")
    public void rentAffordabilityResultShouldBe(String expected) {
        String actual = budgetPlannerPage.getAffordabilityText();
        Assert.assertTrue(actual.contains(expected),
                "Expected affordability result to contain '" + expected + "' but got: " + actual);
    }

    @Then("the results panel should display monthly savings information")
    public void resultsPanelShouldDisplayMonthlySavings() {
        Assert.assertTrue(budgetPlannerPage.isSavingsSectionVisible(),
                "Expected the Monthly Savings section to be visible in the results panel");
    }

    @And("the results panel should display the affordable rent range")
    public void resultsPanelShouldDisplayAffordableRentRange() {
        Assert.assertTrue(budgetPlannerPage.isAffordableRentRangeVisible(),
                "Expected the affordable rent range (Ideal max rent) to be visible in the results panel");
    }

    @Then("the results panel should display an invalid input error message")
    public void resultsPanelShouldDisplayInvalidInputError() {
        Assert.assertTrue(budgetPlannerPage.isInvalidInputErrorVisible(),
                "Expected an invalid-input error alert to be displayed in the results panel");
    }

    @Then("the results panel should display the monthly cost breakdown section")
    public void resultsPanelShouldDisplayCostBreakdown() {
        Assert.assertTrue(budgetPlannerPage.isCostBreakdownVisible(),
                "Expected the Monthly Cost Breakdown section to be visible in the results panel");
    }
}
