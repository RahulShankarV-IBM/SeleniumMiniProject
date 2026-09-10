@US15
Feature: Moving Assistant
  As a user who has found a property
  I want access to legal, document, and moving support tools
  So that I can complete my move smoothly and stay organised

  Background:
    Given I am on the moving assistant page

  # TC71 ──────────────────────────────────────────────────────────────────────
  @TC71 @US15 @Functional @High
  Scenario: Access rental and legal support
    When I click the Legal and Rental Support tab
    Then the legal support panel should be visible
    And the rental agreement accordion should be displayed

  # TC72 ──────────────────────────────────────────────────────────────────────
  @TC72 @US15 @Functional @Medium
  Scenario Outline: Download available documents
    When I click the Documents tab
    And I click the download button for "<document>"
    Then the document download should be triggered

    Examples:
      | document          |
      | Rental Agreement  |
      | Inventory Checklist |
      | KYC Checklist     |

  # TC73 ──────────────────────────────────────────────────────────────────────
  @TC73 @US15 @Functional @Medium
  Scenario: View verification checklists
    When I click the Checklists tab
    Then the tenant documentation checklist should be displayed
    And the property inspection checklist should be displayed
    And the move-in day checklist should be displayed

  # TC74 ──────────────────────────────────────────────────────────────────────
  @TC74 @US15 @Functional @Medium
  Scenario: View moving and setup guidance
    When I click the Moving Guidance tab
    Then the moving guidance panel should be visible

  # TC75 ──────────────────────────────────────────────────────────────────────
  @TC75 @US15 @Functional @Medium
  Scenario: Track moving and setup progress
    When I click the My Progress tab
    Then the progress panel should be visible
    And the overall progress percentage should be displayed
    And the progress bar should be displayed
