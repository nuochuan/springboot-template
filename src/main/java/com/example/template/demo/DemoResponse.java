package com.example.template.demo;

public class DemoResponse {

    private String message;

    public DemoResponse() {
    }

    public DemoResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
