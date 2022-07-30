package com.unibe.titulation.dtos;

public class Message {
    private String message;

    public Message(String text) {
        this.message = text;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String text) {
        this.message = text;
    }
}
