package com.QuetzalSoft.myapplication.ApiModels;

public class StreakCountResponse {
    boolean status;
    String message;

    public StreakCountResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public StreakCountResponse() {
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
