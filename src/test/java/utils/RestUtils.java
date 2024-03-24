package utils;

import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;
import reporting.ExtentReportManager;

public class RestUtils {
    public static void printRequestLogInReport(RequestSpecification requestSpecification){
        QueryableRequestSpecification queryableRequestSpecification = SpecificationQuerier.query(requestSpecification);
        ExtentReportManager.logInfoDetails("Endpoint is: " + queryableRequestSpecification.getBaseUri());
        ExtentReportManager.logInfoDetails("Headers is: " + queryableRequestSpecification.getHeaders().asList().toString());
        ExtentReportManager.logInfoDetails(queryableRequestSpecification.getHeaders().asList().toString());
        ExtentReportManager.logInfoDetails("Request body is: ");
        ExtentReportManager.logJson(queryableRequestSpecification.getBody());
    }

    public static void printResponseLogInReport(Response response){
        ExtentReportManager.logInfoDetails("Response status is: " + response.getStatusCode());
        ExtentReportManager.logInfoDetails("Response headers are : " + response.getHeaders());
        ExtentReportManager.logInfoDetails("Response body is: ");
        ExtentReportManager.logJson(response.getBody().prettyPrint());
    }
}
