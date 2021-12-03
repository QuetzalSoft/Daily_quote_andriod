package com.QuetzalSoft.myapplication.ApiModels;

public class FavResponse {
    String message;
    boolean status;

    public FavResponse(String message, boolean status) {
        this.message = message;
        this.status = status;
    }

    public FavResponse() {
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

    @Override
    public String toString() {
        return "FavResponse{" +
                "message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
