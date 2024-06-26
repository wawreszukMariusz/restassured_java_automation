package tests;

import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import java.io.File;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.testng.Assert.assertNull;
import static org.testng.AssertJUnit.assertEquals;

public class UploadFileTest extends BaseTest{

    @Test
    public void uploadTxtFileTest() {
            File file = new File("src/test/resources/files/txtFile.txt");

            MultiPartSpecBuilder multiPartSpecBuilder = new MultiPartSpecBuilder(file)
                    .controlName("file")
                    .fileName("txtFile.txt")
                    .mimeType("multipart/form-data");

            response = RestAssured.
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
        assertEquals(response.path("filename"), "txtFile.txt");
        assertEquals(response.path("status"), "processed");
        assertNull(response.path("status_details"));
    }

    @Test
    public void uploadJsonFileTest() {
        File file = new File("src/test/resources/files/jsonFile.json");

        MultiPartSpecBuilder multiPartSpecBuilder = new MultiPartSpecBuilder(file)
                .controlName("file")
                .fileName("jsonFile.json")
                .mimeType("multipart/form-data");

        response = RestAssured.
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
    }

    @Test
    public void uploadHtmlFile() {
        File file = new File("src/test/resources/files/htmlFile.html");

        MultiPartSpecBuilder multiPartSpecBuilder = new MultiPartSpecBuilder(file)
                .controlName("file")
                .fileName("htmlFile.html")
                .mimeType("multipart/form-data");

        response = RestAssured.
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
        assertEquals(response.path("filename"), "htmlFile.html");
        assertEquals(response.path("status"), "processed");
        assertNull(response.path("status_details"));
    }

    @Test
    public void uploadUnsupportedFileTest() {
        File file = new File("src/test/resources/files/batFile.bat");

        MultiPartSpecBuilder multiPartSpecBuilder = new MultiPartSpecBuilder(file)
                .controlName("file")
                .fileName("batFile.bat")
                .mimeType("multipart/form-data");

        response = RestAssured.
                given()
                    .spec(reqSpec)
                    .basePath("v1/files")
                    .multiPart(multiPartSpecBuilder.build())
                    .multiPart("purpose", "assistants")
                    .contentType("multipart/form-data").
                when()
                    .post().
                then()
                    .statusCode(400)
                    .spec(resSpec)
                    .body(matchesJsonSchemaInClasspath("schemas/errorSchema.json")).
                and()
                    .body(Matchers.containsString("Invalid file format"))
                    .extract().response();

        assertEquals(response.path("error.type"), "invalid_request_error");
        assertNull(response.path("error.param"));
        assertNull(response.path("error.code"));
    }

    @Test
    public void uploadFileNotProvidedTest() {

        response = RestAssured.
                given()
                .spec(reqSpec)
                .basePath("v1/files")
                .multiPart("purpose", "assistants")
                .contentType("multipart/form-data").
                when()
                .post().
                then()
                .statusCode(400)
                .spec(resSpec)
                .body(matchesJsonSchemaInClasspath("schemas/errorSchema.json"))
                .extract().response();

        assertEquals(response.path("error.message"), "'file' is a required property");
        assertEquals(response.path("error.type"), "invalid_request_error");
        assertNull(response.path("error.param"));
        assertNull(response.path("error.code"));
    }

    @Test
    public void uploadPurposeNotProvidedTest() {
        File file = new File("src/test/resources/files/batFile.bat");

        MultiPartSpecBuilder multiPartSpecBuilder = new MultiPartSpecBuilder(file)
                .controlName("file")
                .fileName("batFile.bat")
                .mimeType("multipart/form-data");

        response = RestAssured.
                given()
                .spec(reqSpec)
                .basePath("v1/files")
                .multiPart(multiPartSpecBuilder.build())
                .contentType("multipart/form-data").
                when()
                .post().
                then()
                .statusCode(400)
                .spec(resSpec)
                .body(matchesJsonSchemaInClasspath("schemas/errorSchema.json"))
                .extract().response();

        assertEquals(response.path("error.message"), "'purpose' is a required property");
        assertEquals(response.path("error.type"), "invalid_request_error");
        assertNull(response.path("error.param"));
        assertNull(response.path("error.code"));
    }

    @Test
    public void uploadWithoutAuthTest() {
        File file = new File("src/test/resources/files/batFile.bat");

        MultiPartSpecBuilder multiPartSpecBuilder = new MultiPartSpecBuilder(file)
                .controlName("file")
                .fileName("batFile.bat")
                .mimeType("multipart/form-data");

        response = RestAssured.
                given()
                .baseUri("https://api.openai.com/")
                .basePath("v1/files")
                .contentType(ContentType.JSON)
                .multiPart(multiPartSpecBuilder.build())
                .contentType("multipart/form-data").
                when()
                .post().
                then()
                .statusCode(401)
                .spec(resSpec)
                .body(matchesJsonSchemaInClasspath("schemas/errorSchema.json"))
                .and()
                .body(Matchers.containsString("You didn't provide an API key."))
                .extract().response();

        assertEquals(response.path("error.type"), "invalid_request_error");
        assertNull(response.path("error.param"));
        assertNull(response.path("error.code"));
    }


}
