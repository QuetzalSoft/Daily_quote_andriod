package com.QuetzalSoft.myapplication.ApiModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShowQuotes {
    @SerializedName("response")
    @Expose
    private List<ShowQuotesMyResponse> response = null;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;

    public ShowQuotes() {
    }

    public List<ShowQuotesMyResponse> getResponse() {
        return response;
    }

    public void setResponse(List<ShowQuotesMyResponse> response) {
        this.response = response;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
