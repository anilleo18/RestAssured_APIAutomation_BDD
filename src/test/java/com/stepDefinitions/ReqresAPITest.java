package com.stepDefinitions;

import dto.UserData;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

import com.mypojo.UserPojo;
import com.utils_pack.JsonPathImpl;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class ReqresAPITest {

    UserData userData;
    UserPojo userPojo;
    Response response;
    String responseBody;
    RequestSpecification requestSpec;
    String userId;

    @Given("As an user")
    public void as_an_user() {
        userData = new UserData();
        userPojo = userData.createUser();
        requestSpec = given()
                .contentType(ContentType.JSON)
                .body(userPojo);
    }

    @When("I add a resource by providing name and job")
    public void i_add_a_resource_by_providing_and() {
        response = requestSpec
                .post("/users");
    }

    @Then("I should get {string} response with id")
    public void i_should_get_response_with_id(String expectedStatusCode) {
        responseBody = response.getBody().asString();
        userId = JsonPathImpl.extractValueFromResponse(responseBody, "id");
        Assert.assertNotNull(userId, "User ID should not be null");
        Assert.assertEquals(response.getStatusCode(), Integer.parseInt(expectedStatusCode), "Status code does not match");
        response.prettyPrint();
        System.out.println("Generated User ID: " + userId);
    }

    @Given("A list of users are available")
    public void a_list_of_users_are_available() {
        requestSpec = given();
    }

    @Given("I specify the pagecount as {string}")
    public void i_specify_the_pagecount_as(String pageCount) {
        requestSpec.queryParam("page", pageCount);
    }

    @When("I fetch the users")
    public void i_fetch_the_users() {
        response = requestSpec.get("/users");
    }

    @Then("I should get {string} response and page value as {string} in response body")
    public void i_should_get_response_and_page_value_as_in_response_body(String expectedStatusCode, String expectedPageValue) {
        response.prettyPrint();
        responseBody = response.getBody().asString();
        String actualPageValue = JsonPathImpl.extractValueFromResponse(responseBody, "page");

        Assert.assertEquals(response.getStatusCode(), Integer.parseInt(expectedStatusCode), "Status code does not match");
        Assert.assertEquals(actualPageValue, expectedPageValue, "Page value does not match");
    }

    @When("I fetch the user by passing id {string}")
    public void i_fetch_the_user_by_passing_id(String userId) {
        response = when().get("/users/" + userId);
    }

    @Then("I should get {string} response and the response body with details")
    public void i_should_get_response_and_the_response_body_with_details(String expectedStatusCode, io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> expectedData = dataTable.asMaps(String.class, String.class);
        responseBody = response.getBody().asString();

        String actualEmail = JsonPathImpl.extractValueFromResponse(responseBody, "data.email");
        String actualFirstName = JsonPathImpl.extractValueFromResponse(responseBody, "data.first_name");
        String actualLastName = JsonPathImpl.extractValueFromResponse(responseBody, "data.last_name");

        Assert.assertEquals(response.getStatusCode(), Integer.parseInt(expectedStatusCode), "Status code does not match");
        Assert.assertEquals(actualEmail, expectedData.get(0).get("email"), "Email does not match");
        Assert.assertEquals(actualFirstName, expectedData.get(0).get("first_name"), "First name does not match");
        Assert.assertEquals(actualLastName, expectedData.get(0).get("last_name"), "Last name does not match");
    }
}
