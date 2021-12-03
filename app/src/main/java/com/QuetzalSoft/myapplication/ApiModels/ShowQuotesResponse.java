package com.QuetzalSoft.myapplication.ApiModels;

import java.util.List;

public class ShowQuotesResponse {

    boolean status;
    String message;
    List<IsFav> is_fav;
    List<response> response;

    public ShowQuotesResponse() {
    }

    public ShowQuotesResponse(boolean status, String message, List<IsFav> is_fav, List<com.QuetzalSoft.myapplication.ApiModels.response> response) {
        this.status = status;
        this.message = message;
        this.is_fav = is_fav;
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

    public List<IsFav> getIs_fav() {
        return is_fav;
    }

    public void setIs_fav(List<IsFav> is_fav) {
        this.is_fav = is_fav;
    }

    public List<com.QuetzalSoft.myapplication.ApiModels.response> getResponse() {
        return response;
    }

    public void setResponse(List<com.QuetzalSoft.myapplication.ApiModels.response> response) {
        this.response = response;
    }
}
