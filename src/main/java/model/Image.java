package model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Image {

    String prompt;
    String model;
    int n;
    String quality;
    String response_format;
    String size;
    String style;
    String user;

    public Image() {
    }
    public Image(String prompt) {
        this.prompt = prompt;
    }

    public Image(String prompt, String model, int n, String quality, String response_format, String size, String style, String user) {
        this.prompt = prompt;
        this.model = model;
        this.n = n;
        this.quality = quality;
        this.response_format = response_format;
        this.size = size;
        this.style = style;
        this.user = user;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getResponse_format() {
        return response_format;
    }

    public void setResponse_format(String response_format) {
        this.response_format = response_format;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return n == image.n && Objects.equals(prompt, image.prompt) && Objects.equals(model, image.model) && Objects.equals(quality, image.quality) && Objects.equals(response_format, image.response_format) && Objects.equals(size, image.size) && Objects.equals(style, image.style) && Objects.equals(user, image.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prompt, model, n, quality, response_format, size, style, user);
    }
}
