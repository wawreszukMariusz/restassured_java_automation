package tests;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeSuite;
import utils.PropertiesLoader;

public class BaseTest {

    protected RequestSpecification reqSpec;
    protected ResponseSpecification resSpec;

    @BeforeSuite
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

    protected String getApiKey() {
        return PropertiesLoader.loadProperty("api-key");
    }

}
