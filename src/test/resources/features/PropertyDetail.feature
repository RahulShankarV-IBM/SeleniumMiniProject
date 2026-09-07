@US05 @US06
Feature: Property Detail Page
  As a user of PropFind
  I want to view full property details and nearby information
  So that I can make an informed decision about a property

  Background:
    Given I am on the property detail page for property 1

  # TC21 ─────────────────────────────────────────────────────────────────────
  @TC21 @US05 @Functional @High
  Scenario: Open property details page
    Then the property detail content should be displayed
    And the property title should be visible
    And the property location should be visible

  # TC22 ─────────────────────────────────────────────────────────────────────
  @TC22 @US05 @Functional @High
  Scenario: Verify price, location, area and BHK
    Then the property price should be displayed
    And the property area should be displayed
    And the property BHK count should be displayed

  # TC23 ─────────────────────────────────────────────────────────────────────
  @TC23 @US05 @Functional @Medium
  Scenario: View property images
    Then the main property image should be loaded
    And the image gallery thumbnails should be visible

  # TC24 ─────────────────────────────────────────────────────────────────────
  @TC24 @US05 @Functional @Medium
  Scenario: View virtual tour
    When I click on the virtual tour section
    Then the virtual tour content should be displayed

  # TC25 ─────────────────────────────────────────────────────────────────────
  @TC25 @US05 @Functional @Medium
  Scenario: View owner/agent information
    Then the owner name should be displayed
    And the owner contact button should be visible

  # TC26 ─────────────────────────────────────────────────────────────────────
  @TC26 @US06 @Functional @High
  Scenario: View nearby schools and hospitals
    Then nearby schools should be listed
    And nearby hospitals should be listed

  # TC27 ─────────────────────────────────────────────────────────────────────
  @TC27 @US06 @Functional @High
  Scenario: View nearby emergency facilities
    Then nearby police station information should be displayed
    And nearby fire station information should be displayed

  # TC28 ─────────────────────────────────────────────────────────────────────
  @TC28 @US06 @Functional @Medium
  Scenario: View nearby supermarkets and transport
    Then nearby supermarkets should be listed
    And nearby metro stations should be listed

  # TC29 ─────────────────────────────────────────────────────────────────────
  @TC29 @US06 @Functional @Medium
  Scenario: View locality insights
    Then the locality insights section should be displayed
    And locality scores should be visible

  # TC30 ─────────────────────────────────────────────────────────────────────
  @TC30 @US06 @Functional @High
  Scenario: Access emergency support information
    Then the safety and security section should be displayed
    And CCTV and gated community information should be visible
