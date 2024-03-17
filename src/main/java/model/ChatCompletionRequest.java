package model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatCompletionRequest {
    private List<Message> messages;
    private String model;

    public ChatCompletionRequest() {
    }

    public ChatCompletionRequest(List<Message> messages, String model) {
        this.messages = messages;
        this.model = model;
    }
    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatCompletionRequest that = (ChatCompletionRequest) o;
        return Objects.equals(messages, that.messages) && Objects.equals(model, that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messages, model);
    }
}