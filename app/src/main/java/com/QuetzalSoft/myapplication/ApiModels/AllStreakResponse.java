package com.QuetzalSoft.myapplication.ApiModels;

import java.util.List;

public class AllStreakResponse {
    List<response> response;

    public AllStreakResponse(List<response> response) {
        this.response = response;
    }

    public List<AllStreakResponse.response> getResponse() {
        return response;
    }

    public void setResponse(List<AllStreakResponse.response> response) {
        this.response = response;
    }

   public class response{
        String streak_count;

        public response(String streak_count) {
            this.streak_count = streak_count;
        }

        public String getStreak_count() {
            return streak_count;
        }

        public void setStreak_count(String streak_count) {
            this.streak_count = streak_count;
        }
    }
}
