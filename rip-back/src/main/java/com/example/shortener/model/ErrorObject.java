package com.example.shortener.model;

public class ErrorObject {
    private String error;

    public ErrorObject() {
    }

    public ErrorObject(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
