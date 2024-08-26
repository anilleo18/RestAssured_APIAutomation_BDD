import base.BaseTest;
import io.restassured.parsing.Parser;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.annotations.CustomFrameworkAnnotations;
import com.my_enumerations.TestCaseType;
import com.utils_pack.JsonPathImpl;

import static io.restassured.RestAssured.given;

public class Test_Auth extends BaseTest {

    @CustomFrameworkAnnotations(testCaseType = TestCaseType.INTEGRATION)
    @Test(testName = "Validate authentication error when there is no access token")
    public void validateAuthenticationError() {
        String response = given()
                .queryParam("access_token", "")
                .when()
                .get("https://example.com/getCourse.php")
                .then()
                .extract()
                .response()
                .asString();

        System.out.println("Response: " + response);

        Assert.assertEquals(response.trim(), "AUTHENTICATION FAILED !!!! PLEASE ENTER VALID ACCESS TOKEN");
    }

    @CustomFrameworkAnnotations(testCaseType = TestCaseType.E2E)
    @Test(testName = "Validate obtaining Authorization code and Access token to fetch the course list")
    public void validateAuthorizationAndAccessTokenToFetchDetails() {

        String url = "https://example.com/getCourse.php?state=teststate&code=some_auth_code&scope=email+profile&authuser=0&session_state=example_session&prompt=none#";

        String code = extractAuthorizationCode(url);
        System.out.println("Authorization Code: " + code);

        String response = given()
                .urlEncodingEnabled(false)
                .queryParams("code", code)
                .queryParams("client_id", "hkfbewhkfiygfygfgkhwebfhkfgewiyfgwei")
                .queryParams("client_secret", "dfbdwkbfewegf8787gfwefb87g78wg7847486")
                .queryParams("grant_type", "authorization_code")
                .queryParams("state", "teststate")
                .queryParams("session_state", "example_session_state")
                .queryParams("redirect_uri", "https://example.com/getCourse.php")
                .when().log().all()
                .post("https://oauth2.googleapis.com/token")
                .asString();

        System.out.println("Response: " + response);

        String accessToken = JsonPathImpl.extractValueFromResponse(response, "access_token");
        System.out.println("Access Token: " + accessToken);

        String courseDetailsResponse = given()
                .contentType("application/json")
                .queryParam("access_token", accessToken)
                .expect().defaultParser(Parser.JSON)
                .when()
                .get("https://example.com/getCourse.php")
                .asString();

        System.out.println("Course Details: " + courseDetailsResponse);
    }

    private String extractAuthorizationCode(String url) {
        String[] parts = url.split("code=");
        String[] codeParts = parts[1].split("&scope");
        return codeParts[0];
    }
}
