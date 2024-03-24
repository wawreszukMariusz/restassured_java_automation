package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.Image;
import org.testng.annotations.Test;
import java.util.List;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.testng.AssertJUnit.assertEquals;

public class CreateImageTest extends BaseTest {

    @Test
    public void createSingleImageTest() {
        Image image = new Image("Typical tester at work", "dall-e-2", 1, "standard", "url", "1024x1024", "vivid", "Tester");

        response = RestAssured.
                given()
                .spec(reqSpec)
                .basePath("v1/images/generations")
                .contentType(ContentType.JSON)
                .body(image).
                when()
                .post().
                then()
                .statusCode(200)
                .spec(resSpec)
                .body(matchesJsonSchemaInClasspath("schemas/createImageSchema.json")).extract().response();

        List<Object> dataList = response.path("data");
        int dataSize = dataList.size();

        assertEquals(dataSize, 1);
    }

    @Test
    public void createTwoImagesTest() {
        Image image = new Image("Typical tester", "dall-e-2", 2, "standard", "url", "1024x1024", "vivid", "Tester");

        response = RestAssured.
                given()
                .spec(reqSpec)
                .basePath("v1/images/generations")
                .contentType(ContentType.JSON)
                .body(image).
                when()
                .post().
                then()
                .statusCode(200)
                .spec(resSpec)
                .body(matchesJsonSchemaInClasspath("schemas/createImageSchema.json")).extract().response();

        List<Object> dataList = response.path("data");
        int dataSize = dataList.size();

        assertEquals(dataSize, 2);
    }
}
