Feature: Comprehensive Testing for Reqres API

  @Integration
  Scenario: Successfully create a new user resource
    Given I am a registered user
    When I create a new resource with a name and job title
    Then I should receive a "201" status code along with a unique id

  @Integration
  Scenario: Retrieve a list of existing users
    Given Multiple users exist in the system
    And I specify the page number as "2"
    When I request the user list
    Then I should receive a "200" status code and the page value should be "2" in the response

  @EndToEnd
  Scenario: Retrieve details of a specific user
    Given I am a registered user
    When I request details of the user with id "2"
    Then I should receive a "200" status code and the following user information
      | email                  | first_name | last_name |
      | anilleo18@gmail.com | anilp      | leo18    |
