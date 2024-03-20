package tests;

import io.restassured.response.Response;
import model.ChatCompletionRequest;
import model.Message;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNull;

public class ListFilesTest extends BaseTest{


    @Test
    public void getFilesListTest() {
        Response response =
                        given()
                            .spec(reqSpec)
                            .basePath("v1/files").
                        when()
                            .get().
                        then()
                            .statusCode(200)
                            .spec(resSpec)
                            .body(matchesJsonSchemaInClasspath("schemas/filesListSchema.json"))
                            .extract().response();
    }

    @Test
    public void getFilesListWithAssistantsPurpose() {
        Response response =
                        given()
                            .spec(reqSpec)
                            .queryParam("purpose", "assistants").
                        when()
                            .get("/v1/files").
                        then()
                            .statusCode(200)
                            .spec(resSpec)
                            .body(matchesJsonSchemaInClasspath("schemas/filesListSchema.json"))
                            .extract().response();
    }

    @Test
    public void getFilesListWithEmptyListPurpose() {
        Response response =
                given()
                        .spec(reqSpec)
                        .queryParam("purpose", "fine-tune").
                        when()
                        .get("/v1/files").
                        then()
                        .statusCode(200)
                        .spec(resSpec)
                        .body(matchesJsonSchemaInClasspath("schemas/filesListSchema.json"))
                        .extract().response();
    }

    @Test
    public void getFilesListWithNotExistingPurpose() {
        Response response =
                        given()
                            .spec(reqSpec)
                            .queryParam("purpose", "test").
                        when()
                            .get("/v1/files").
                        then()
                            .statusCode(400)
                            .spec(resSpec)
                            .body(matchesJsonSchemaInClasspath("schemas/errorSchema.json"))
                            .extract().response();

        assertEquals(response.path("error.message"), "Invalid purpose: test");
        assertEquals(response.path("error.type"), "invalid_request_error");
        assertNull(response.path("error.param"));
        assertNull(response.path("error.code"));
    }
}
