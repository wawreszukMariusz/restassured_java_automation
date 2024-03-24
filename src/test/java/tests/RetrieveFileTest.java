package tests;

import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.io.File;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.testng.AssertJUnit.*;

public class RetrieveFileTest extends BaseTest {
    @Test
    public void retrieveFileTest() {
        File file = new File("src/test/resources/files/jsonFile.json");

        MultiPartSpecBuilder multiPartSpecBuilder = new MultiPartSpecBuilder(file)
                .controlName("file")
                .fileName("jsonFile.json")
                .mimeType("multipart/form-data");

        Response uploadResponse = RestAssured.
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

        assertEquals(uploadResponse.path("object"), "file");
        assertEquals(uploadResponse.path("purpose"), "assistants");
        assertEquals(uploadResponse.path("filename"), "jsonFile.json");
        assertEquals(uploadResponse.path("status"), "processed");
        assertNull(uploadResponse.path("status_details"));

        String fileId = uploadResponse.path("id").toString();

         response =
                given()
                        .spec(reqSpec)
                        .pathParam("file_id", fileId).
                when()
                        .get("/v1/files/{file_id}").
                then()
                        .statusCode(200)
                        .spec(resSpec)
                        .body(matchesJsonSchemaInClasspath("schemas/retrieveFileSchema.json"))
                        .extract().response();

        assertEquals(response.path("id"), fileId);
        assertEquals(response.path("object"), "file");
        assertEquals(response.path("bytes").toString(), "24");
        assertEquals(response.path("filename"), "jsonFile.json");
        assertEquals(response.path("purpose"), "assistants");
        assertEquals(response.path("status"), "processed");
        assertNull(response.path("status_details"));
    }

    @Test
    public void retrieveNotExistingFileTest() {
        response =
                given()
                            .spec(reqSpec)
                            .pathParam("file_id", "test").
                        when()
                            .get("/v1/files/{file_id}").
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
