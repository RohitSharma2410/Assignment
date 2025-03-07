@Base
Feature: This test is to demonstrate assignment for TEG


  Scenario: Positive Scenario to check fact field string length greater than 5
    Given API to call is "https://catfact.ninja/fact"
    When send request
    Then "fact" field should be having value length greater than <5>

  Scenario: Negavtive Scenario to check response code 404
    Given API to call is "https://catfact.ninja/fact1"
    When send request
    Then response code should be <404>
