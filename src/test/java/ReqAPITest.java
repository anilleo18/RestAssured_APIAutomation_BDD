import base.BaseTest;
import dto.UserData;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.annotations.CustomFrameworkAnnotations;
import com.my_enumerations.ResSpecs_request;
import com.my_enumerations.TestCaseType;
import com.mypojo.UserPojo;
import com.utils_pack.JsonPathImpl;
import com.utils_pack.PropertiesFileImpl;
import com.utils_pack.ReqSpecsImpl;

import static io.restassured.RestAssured.given;

public class ReqAPITest extends BaseTest {

    private RequestSpecification specs;
    private Response response;
    private UserData udata;
    private UserPojo upojo;
    private String responseString;

    private static final String LOG_FILE_NAME = "ReqAPITests";

    @BeforeTest(alwaysRun = true)
    public void initialiseSetup() {
        ReqSpecsImpl reqSpecs = new ReqSpecsImpl();
        specs = reqSpecs.setRequestSpecs(
                PropertiesFileImpl.getDataFromPropertyFile(ResSpecs_request.RESURI_REQUEST),
                PropertiesFileImpl.getDataFromPropertyFile(ResSpecs_request.BASEPATH_REQUEST),
                LOG_FILE_NAME
        );
    }

    @CustomFrameworkAnnotations(testCaseType = TestCaseType.INTEGRATION)
    @Test(testName = "Validate getting list of users based on Query params")
    public void getListOfUsers() {
        // Executing the GET request with query parameter and logging the response
        response = given(specs)
                .queryParam("page", "2")
                .when()
                .get("/users");

        response.prettyPrint();  // Log the entire response for debugging

        responseString = response.asString();  // Storing the response as a String for further use
        String extractedPage = JsonPathImpl.extractValueFromResponse(responseString, "page");  // Extracting 'page' value

        // Asserting the HTTP status code and validating the 'page' field from the response
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code did not match.");
        Assert.assertEquals(extractedPage, "2", "The page number in the response is not as expected.");
    }

    @CustomFrameworkAnnotations(testCaseType = TestCaseType.INTEGRATION)
    @Test(testName = "Validate creation of user")
    public void createAnUser() {
        udata = new UserData();  // Initializing UserData to create a new user
        upojo = udata.createUser();  // Creating a user POJO with user data

        // Executing the POST request to create a user
        response = given(specs)
                .body(upojo)
                .when()
                .post("/users");

        responseString = response.asString();  // Storing the response as a String for further use
        String userId = JsonPathImpl.extractValueFromResponse(responseString, "id");  // Extracting the user ID

        // Asserting that the user ID is not null and that the HTTP status code is 201 (Created)
        Assert.assertNotNull(userId, "User ID should not be null after user creation.");
        Assert.assertEquals(response.getStatusCode(), 201, "Expected status code did not match.");

        response.prettyPrint();  // Log the entire response for debugging
        System.out.println("Created User ID: " + userId);  // Output the user ID for confirmation
    }
}
