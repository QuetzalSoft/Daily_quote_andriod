package com.QuetzalSoft.myapplication.ApiModels;

import java.util.List;

public class AllFavsResponse {

    boolean status;
    String message;
    List<response> response;

    public AllFavsResponse(boolean status, String message, List<com.QuetzalSoft.myapplication.ApiModels.response> response) {
        this.status = status;
        this.message = message;
        this.response = response;
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

    public List<com.QuetzalSoft.myapplication.ApiModels.response> getResponse() {
        return response;
    }

    public void setResponse(List<com.QuetzalSoft.myapplication.ApiModels.response> response) {
        this.response = response;
    }
}

