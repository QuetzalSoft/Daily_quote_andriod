package com.QuetzalSoft.myapplication.ApiModels;

public class QuotesInfoResponse {

    String message;
    boolean status;
    response response;

    public QuotesInfoResponse(String message, boolean status, QuotesInfoResponse.response response) {
        this.message = message;
        this.status = status;
        this.response = response;
    }

    public QuotesInfoResponse() {
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

    public QuotesInfoResponse.response getResponse() {
        return response;
    }

    public void setResponse(QuotesInfoResponse.response response) {
        this.response = response;
    }

    public class response{
        String streak_count, is_fav;

        public response(String streak_count, String is_fav) {
            this.streak_count = streak_count;
            this.is_fav = is_fav;
        }

        public response() {
        }

        public String getStreak_count() {
            return streak_count;
        }

        public void setStreak_count(String streak_count) {
            this.streak_count = streak_count;
        }

        public String getIs_fav() {
            return is_fav;
        }

        public void setIs_fav(String is_fav) {
            this.is_fav = is_fav;
        }
    }
}
