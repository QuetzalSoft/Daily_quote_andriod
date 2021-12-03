package com.QuetzalSoft.myapplication.ApiModels;

public class ForgetPasswordResponse {
    String message;
    boolean status;

    public ForgetPasswordResponse(String message, boolean status) {
        this.message = message;
        this.status = status;
    }

    public ForgetPasswordResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
