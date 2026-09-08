@US08
Feature: Compare Properties
  As a user of PropFind
  I want to compare properties side by side
  So that I can make an informed property decision

  Background:
    Given I am on the compare page

  # TC36 ─────────────────────────────────────────────────────────────────────
  @TC36 @US08 @Functional @High
  Scenario: Compare three properties
    Given the session contains property IDs 1, 2, and 3
    When I load the compare page
    Then three property columns should be displayed in the comparison table

  # TC37 ─────────────────────────────────────────────────────────────────────
  @TC37 @US08 @Functional @High
  Scenario: Compare property details side by side
    Given the session contains property IDs 1 and 2
    When I load the compare page
    Then the comparison table should display price, BHK, location and amenities for each property

  # TC38 ─────────────────────────────────────────────────────────────────────
  @TC38 @US08 @Functional @Medium
  Scenario: Compare nearby and locality information
    Given the session contains property IDs 1 and 2
    When I load the compare page
    Then the comparison table should display nearby school, hospital and metro information

  # TC39 ─────────────────────────────────────────────────────────────────────
  @TC39 @US08 @Negative @High
  Scenario: Show empty state when fewer than two properties are selected
    Given the session has no properties selected for comparison
    When I load the compare page
    Then the empty comparison message should be displayed

  # TC40 ─────────────────────────────────────────────────────────────────────
  # TC40 ─────────────────────────────────────────────────────────────────────
@TC40 @US08 @Functional @Medium
Scenario: Remove a property from the comparison
    Given the session contains property IDs 1, 2, and 3
    When I load the compare page
    And I remove property with ID 1 from the comparison
    Then two property columns should be displayed in the comparison table