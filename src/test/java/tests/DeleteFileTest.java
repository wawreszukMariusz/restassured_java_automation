package tests;

import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.testng.AssertJUnit.*;

public class DeleteFileTest extends BaseTest{

    @Test
    public void deleteFileTest() {
        File file = new File("src/test/resources/files/jsonFile.json");

        MultiPartSpecBuilder multiPartSpecBuilder = new MultiPartSpecBuilder(file)
                .controlName("file")
                .fileName("jsonFile.json")
                .mimeType("multipart/form-data");

        Response response = RestAssured.
                given()
                .spec(reqSpec)
                .basePath("v1/files")
                .multiPart(multiPartSpecBuilder.build())
                .multiPart("purpose", "assistants")
                .contentType("multipart/form-data").
                when()
                .post().
                then()
                .statusCode(200)
                .spec(resSpec)
                .body(matchesJsonSchemaInClasspath("schemas/uploadFileSchema.json")).extract().response();

        assertEquals(response.path("object"), "file");
        assertEquals(response.path("purpose"), "assistants");
        assertEquals(response.path("filename"), "jsonFile.json");
        assertEquals(response.path("status"), "processed");
        assertNull(response.path("status_details"));

        String fileId = response.path("id").toString();

        Response deleteResponse =
                given()
                        .spec(reqSpec)
                        .pathParam("file_id", fileId).
                        when()
                        .delete("/v1/files/{file_id}").
                        then()
                        .statusCode(200)
                        .spec(resSpec)
                        .body(matchesJsonSchemaInClasspath("schemas/deleteFileSchema.json"))
                        .extract().response();

        assertEquals(deleteResponse.path("object"), "file");
        assertTrue(deleteResponse.path("deleted"));
        assertEquals(deleteResponse.path("id"), fileId);
    }

    @Test
    public void deleteNotExistingFileTest() {
        Response response =
                given()
                        .spec(reqSpec)
                        .pathParam("file_id", "test").
                        when()
                        .delete("/v1/files/{file_id}").
                        then()
                        .statusCode(404)
                        .spec(resSpec)
                        .body(matchesJsonSchemaInClasspath("schemas/errorSchema.json"))
                        .extract().response();

        assertEquals(response.path("error.message"), "No such File object: test");
        assertEquals(response.path("error.type"), "invalid_request_error");
        assertEquals(response.path("error.param"), "id");
        assertNull(response.path("error.code"));
    }
}
