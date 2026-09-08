@US12
Feature: Budget Planner
  As a user of PropFind
  I want to plan my housing budget
  So that I can understand my total monthly costs and affordability before choosing a property

  Background:
    Given I am on the budget planner page

  # TC56 ─────────────────────────────────────────────────────────────────────
  @TC56 @US12 @Functional @High
  Scenario: Calculate total living cost
    When I enter a monthly salary of "80000"
    And I set the rent to "25000"
    And I enter maintenance of "2000"
    And I enter electricity of "1500"
    And I click the Calculate Budget button
    Then the results panel should display the total monthly expenses

  # TC57 ─────────────────────────────────────────────────────────────────────
  @TC57 @US12 @Functional @High
  Scenario Outline: Calculate affordability
    When I enter a monthly salary of "<salary>"
    And I set the rent to "<rent>"
    And I click the Calculate Budget button
    Then the rent affordability result should be "<affordability>"

    Examples:
      | salary | rent   | affordability |
      | 80000  | 20000  | Affordable    |
      | 50000  | 30000  | High          |

  # TC58 ─────────────────────────────────────────────────────────────────────
  @TC58 @US12 @Functional @High
  Scenario: Calculate affordable rent range and estimated savings
    When I enter a monthly salary of "60000"
    And I set the rent to "15000"
    And I enter food expenses of "8000"
    And I enter transport expenses of "3000"
    And I click the Calculate Budget button
    Then the results panel should display monthly savings information
    And the results panel should display the affordable rent range

  # TC59 ─────────────────────────────────────────────────────────────────────
  @TC59 @US12 @Negative @High
  Scenario: Show validation message for invalid financial values
    When I enter an invalid value "abc" in the salary field
    And I click the Calculate Budget button
    Then the results panel should display an invalid input error message

  # TC60 ─────────────────────────────────────────────────────────────────────
  @TC60 @US12 @Functional @Medium
  Scenario: Display cost breakdown summary after calculation
    When I enter a monthly salary of "70000"
    And I set the rent to "20000"
    And I click the Calculate Budget button
    Then the results panel should display the monthly cost breakdown section
