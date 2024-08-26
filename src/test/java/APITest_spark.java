import com.annotations.CustomFrameworkAnnotations;
import com.github.javafaker.Faker;
import com.my_enumerations.Specsspark;
import com.my_enumerations.TestCaseType;
import com.utils_pack.JsonPathImpl;
import com.utils_pack.PropertiesFileImpl;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;

public class APITest_spark {

    private RequestSpecification requestSpec;
    private Response response;
    private String responseString;
    private String customerId;

    private static final String MISSING_API_KEY_ERROR_MSG = "You did not provide an API key. You need to provide your API key in the Authorization header, using Bearer auth (e.g. 'Authorization: Bearer YOUR_SECRET_KEY'). See https://stripe.com/docs/api#authentication for details, or we can help at https://support.stripe.com/.";
    private static final String MISSING_API_KEY_ERROR_TYPE = "invalid_request_error";

    @BeforeClass(alwaysRun = true)
    public void setup() {
        RestAssured.baseURI = PropertiesFileImpl.getDataFromPropertyFile(Specsspark.BASEURI_SPARK);
        RestAssured.basePath = PropertiesFileImpl.getDataFromPropertyFile(Specsspark.BASEPATH__SPARK);
        requestSpec = given()
                .auth()
                .basic(PropertiesFileImpl.getDataFromPropertyFile(Specsspark.SECRETKEY__SPARK), "");
    }

    @Ignore
    @Test(testName = "Check Empty Customer List When No Records Exist")
    public void validateEmptyCustomerList() {

        response = requestSpec.get(PropertiesFileImpl.getDataFromPropertyFile(Specsspark.CUSTOMERAPIENDPOINT__SPARK));
        response.prettyPrint();

        responseString = response.asString();
        JsonPath jsonResponse = JsonPathImpl.rawToJSON(responseString);
        int customerCount = jsonResponse.getList("data").size();

        response.then().assertThat().statusCode(200);
        Assert.assertEquals(customerCount, 0);
    }

    @Test(testName = "Test Customer Creation Without Request Body", priority = 0)
    public void validateCustomerCreationWithoutBody() {
        response = requestSpec.contentType(ContentType.ANY)
                .when()
                .post(PropertiesFileImpl.getDataFromPropertyFile(Specsspark.CUSTOMERAPIENDPOINT__SPARK));
        response.prettyPrint();
        responseString = response.asString();

        String id = JsonPathImpl.extractValueFromResponse(responseString, "id");
        response.then().assertThat().statusCode(200);
        Assert.assertNotNull(id, "Customer ID should not be null");
    }

    @Test(testName = "Create Customer Using Form Parameters", priority = 1)
    public void validateCustomerCreationWithFormParams() {
        Faker faker = new Faker();
        String email = faker.internet().emailAddress();
        String name = faker.name().firstName();
        
        response = requestSpec.config(RestAssured.config().encoderConfig(encoderConfig().encodeContentTypeAs("*/*", ContentType.TEXT)))
                .formParam("name", name)
                .formParam("email", email)
                .when()
                .post(PropertiesFileImpl.getDataFromPropertyFile(Specsspark.CUSTOMERAPIENDPOINT__SPARK));
        responseString = response.asString();

        customerId = JsonPathImpl.extractValueFromResponse(responseString, "id");
        String actualName = JsonPathImpl.extractValueFromResponse(responseString, "name");
        String actualEmail = JsonPathImpl.extractValueFromResponse(responseString, "email");

        response.then().assertThat().statusCode(200);
        Assert.assertEquals(actualName, name, "The name should match the one provided");
        Assert.assertEquals(actualEmail, email, "The email should match the one provided");
    }

    @Test(testName = "Attempt Customer Creation Without Authentication", priority = 3)
    public void validateCustomerCreationWithoutAuthentication() {
        response = given()
                .when()
                .post(PropertiesFileImpl.getDataFromPropertyFile(Specsspark.CUSTOMERAPIENDPOINT__SPARK));
        response.prettyPrint();
        responseString = response.asString();

        response.then().assertThat().statusCode(401);
        String errorMessage = JsonPathImpl.extractValueFromResponse(responseString, "error.message");
        String errorType = JsonPathImpl.extractValueFromResponse(responseString, "error.type");

        Assert.assertEquals(errorMessage, MISSING_API_KEY_ERROR_MSG, "Error message for missing API key should match");
        Assert.assertEquals(errorType, MISSING_API_KEY_ERROR_TYPE, "Error type for missing API key should match");
    }

    @Test(testName = "Validate Customer List with Limited Records", priority = 4)
    public void validateCustomerList() {

        response = requestSpec.queryParam("limit", "3")
                .get(PropertiesFileImpl.getDataFromPropertyFile(Specsspark.CUSTOMERAPIENDPOINT__SPARK));
        response.prettyPrint();
        response.then().assertThat().statusCode(200);

        responseString = response.asString();
        JsonPath jsonResponse = JsonPathImpl.rawToJSON(responseString);
        int customerCount = jsonResponse.getList("data").size();
        Assert.assertEquals(customerCount, 3, "The customer count should match the limit set in query param");
    }

    @Test(testName = "Fetch Customer Details by ID", dependsOnMethods = {"validateCustomerCreationWithFormParams"}, priority = 5)
    public void validateCustomerFetchById() {

        response = requestSpec.pathParam("id", customerId)
                .when()
                .get(PropertiesFileImpl.getDataFromPropertyFile(Specsspark.CUSTOMERAPIENDPOINT__SPARK) + "/{id}");
        response.prettyPrint();
        responseString = response.asString();

        // Additional validation can be performed here if necessary
    }
}
