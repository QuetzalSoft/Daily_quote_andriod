package com.QuetzalSoft.myapplication.ApiModels;

public class IsFav {

    String quote_id;

    public IsFav(String quote_id) {
        this.quote_id = quote_id;
    }

    public IsFav() {
    }

    public String getQuote_id() {
        return quote_id;
    }

    public void setQuote_id(String quote_id) {
        this.quote_id = quote_id;
    }
}
