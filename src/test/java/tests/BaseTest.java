package tests;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import utils.PropertiesLoader;
import utils.RestUtils;

public class BaseTest {

    protected RequestSpecification reqSpec;
    protected ResponseSpecification resSpec;
    protected Response response;

    @BeforeClass
    public void setUp(){
        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer " + getApiKey())
                .setBaseUri("https://api.openai.com/")
                .setContentType(ContentType.JSON).build();

        resSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON).build();

        RequestLoggingFilter reqLog = new RequestLoggingFilter();
        ResponseLoggingFilter resLog = new ResponseLoggingFilter();
        RestAssured.filters(reqLog, resLog);
    }

    @AfterMethod
    public void afterTest(){
        RestUtils.printRequestLogInReport(reqSpec);
        RestUtils.printResponseLogInReport(response);
    }

    protected String getApiKey() {
        return PropertiesLoader.loadProperty("api-key");
    }
}
