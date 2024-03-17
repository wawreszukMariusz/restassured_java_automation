package tests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import java.util.*;
import model.Message;
import model.ChatCompletionRequest;
import utils.Helper;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNull;

public class CreateChatCompletionsTest extends BaseTest{

    @Test
    public void createChatCompletionTest() {
        List<Message> messages = new ArrayList<>();
        messages.add(new Message("system", "Tell me about yourself"));
        messages.add(new Message("user", "Tester"));

        ChatCompletionRequest chatCompletion = new ChatCompletionRequest(messages, "gpt-3.5-turbo-0125");

        Response response =
                given()
                    .spec(reqSpec)
                    .basePath("v1/chat/completions")
                    .body(chatCompletion).
                when()
                    .post().
                then()
                    .statusCode(200)
                    .spec(resSpec)
                    .body(matchesJsonSchemaInClasspath("schemas/chatCompletionSchema.json"))
                    .extract().response();

        assertEquals(response.path("choices[0].finish_reason"), "stop");
        assertEquals(response.path("choices[0].message.role"), "assistant");
        assertEquals(response.path("model"), "gpt-3.5-turbo-0125");
    }


    @Test
    public void tooLongMessageTest() {
        String tooLongMessage = Helper.readTextFromFile("longMessage.txt");

        List<Message> messages = new ArrayList<>();
        messages.add(new Message("system", tooLongMessage));
        messages.add(new Message("user", tooLongMessage));

        ChatCompletionRequest chatCompletion = new ChatCompletionRequest(messages, "gpt-3.5-turbo-0125");

        Response response =
                given()
                    .spec(reqSpec)
                    .basePath("v1/chat/completions")
                    .body(chatCompletion).
                when()
                    .post().
                then()
                    .statusCode(400)
                    .spec(resSpec)
                    .body(matchesJsonSchemaInClasspath("schemas/errorSchema.json"))
                    .extract().response();

        assertEquals(response.path("error.message"), "This model's maximum context length is 16385 tokens. However, your messages resulted in 21269 tokens. Please reduce the length of the messages.");
        assertEquals(response.path("error.type"), "invalid_request_error");
        assertEquals(response.path("error.param"), "messages");
        assertEquals(response.path("error.code"), "context_length_exceeded");
    }

    @Test
    public void emptyMessageProvidedTest() {
        List<Message> messages = new ArrayList<>();
        messages.add(new Message("system", ""));
        messages.add(new Message("user", ""));

        ChatCompletionRequest chatCompletion = new ChatCompletionRequest(messages, "gpt-3.5-turbo-0125");

        Response response =
                given()
                    .spec(reqSpec)
                    .basePath("v1/chat/completions")
                    .body(chatCompletion).
                when()
                    .post().
                then()
                    .statusCode(200)
                    .spec(resSpec)
                    .body(matchesJsonSchemaInClasspath("schemas/chatCompletionSchema.json"))
                    .extract().response();

        assertEquals(response.path("choices[0].finish_reason"), "stop");
        assertEquals(response.path("choices[0].message.role"), "assistant");
        assertEquals(response.path("message[0].content"), "Hello! How can I assist you today?");
    }

    @Test
    public void createChatCompletionForNewChatModelWithoutPermissionTest() {
        List<Message> messages = new ArrayList<>();
        messages.add(new Message("system", "Test"));
        messages.add(new Message("user", "Tester"));

        ChatCompletionRequest chatCompletion = new ChatCompletionRequest(messages, "gpt-4-turbo-preview");

        Response response =
                given()
                    .spec(reqSpec)
                    .basePath("v1/chat/completions")
                    .body(chatCompletion).
                when()
                    .post("https://api.openai.com/v1/chat/completions").
                then()
                    .statusCode(404)
                    .spec(resSpec)
                    .body(matchesJsonSchemaInClasspath("schemas/errorSchema.json"))
                    .extract().response();

        assertEquals(response.path("error.message"), "The model `gpt-4-turbo-preview` does not exist or you do not have access to it. Learn more: https://help.openai.com/en/articles/7102672-how-can-i-access-gpt-4.");
        assertEquals(response.path("error.type"), "invalid_request_error");
        assertNull(response.path("error.param"));
        assertEquals(response.path("error.code"), "model_not_found");
    }

    @Test
    public void createChatCompletionWithUnfavorableMessageTest() {
        List<Message> messages = new ArrayList<>();
        messages.add(new Message("system", "Test"));
        messages.add(new Message("user", "What do you think about programming in Java?"));

        ChatCompletionRequest chatCompletion = new ChatCompletionRequest(messages, "gpt-3.5-turbo-0125");

        Response response =
                given()
                    .spec(reqSpec)
                    .basePath("v1/chat/completions")
                    .body(chatCompletion).
                when()
                    .post("https://api.openai.com/v1/chat/completions").
                then()
                    .statusCode(200)
                    .spec(resSpec)
                    .body(matchesJsonSchemaInClasspath("schemas/chatCompletionSchema.json")).
                and()
                    .body(Matchers.containsString("I don't have personal opinions"))
                    .extract().response();

        assertEquals(response.path("choices[0].finish_reason"), "stop");
        assertEquals(response.path("choices[0].message.role"), "assistant");
    }

    @Test
    public void createChatCompletionWithTranslationRequestTest() {
        List<Message> messages = new ArrayList<>();
        messages.add(new Message("system", "Test"));
        messages.add(new Message("user", "Translate the following English text to French: Ben has a cat"));

        ChatCompletionRequest chatCompletion = new ChatCompletionRequest(messages, "gpt-3.5-turbo-0125");

        Response response =
                given()
                        .spec(reqSpec)
                        .basePath("v1/chat/completions")
                        .body(chatCompletion).
                when()
                        .post("https://api.openai.com/v1/chat/completions").
                then()
                        .spec(resSpec)
                        .statusCode(200)
                        .body(matchesJsonSchemaInClasspath("schemas/chatCompletionSchema.json"))
                        .extract().response();

        assertEquals(response.path("choices[0].finish_reason"), "stop");
        assertEquals(response.path("choices[0].message.role"), "assistant");
        assertEquals(response.path("choices[0].message.content"), "Ben a un chat");
    }

    @Test
    public void checkBasicMathAbilityTest() {
        List<Message> messages = new ArrayList<>();
        messages.add(new Message("system", "Test"));
        messages.add(new Message("user", "How much will it be: 2+2*2"));

        ChatCompletionRequest chatCompletion = new ChatCompletionRequest(messages, "gpt-3.5-turbo-0125");

        Response response =
                given()
                        .spec(reqSpec)
                        .basePath("v1/chat/completions")
                        .body(chatCompletion).
                when()
                        .post("https://api.openai.com/v1/chat/completions").
                then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .body(matchesJsonSchemaInClasspath("schemas/chatCompletionSchema.json")).
                and()
                        .body(Matchers.containsString("is 6"))
                        .extract().response();

        assertEquals(response.path("choices[0].finish_reason"), "stop");
        assertEquals(response.path("choices[0].message.role"), "assistant");
    }
}
