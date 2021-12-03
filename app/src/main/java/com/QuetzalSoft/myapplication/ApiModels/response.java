package com.QuetzalSoft.myapplication.ApiModels;

public class response {
    int id;
    String user_id,
            quote_id,
            is_fav,
            name,
            author_name,
            audio_lesson,
            image,
            color_code,
            show_at,
            status,
            created_at,
            updated_at;

    public response() {
    }

    public response(int id, String user_id, String quote_id, String is_fav,
                    String name, String author_name, String audio_lesson, String image,
                    String color_code, String show_at, String status, String created_at, String updated_at) {
        this.id = id;
        this.user_id = user_id;
        this.quote_id = quote_id;
        this.is_fav = is_fav;
        this.name = name;
        this.author_name = author_name;
        this.audio_lesson = audio_lesson;
        this.image = image;
        this.color_code = color_code;
        this.show_at = show_at;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getQuote_id() {
        return quote_id;
    }

    public void setQuote_id(String quote_id) {
        this.quote_id = quote_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getAudio_lesson() {
        return audio_lesson;
    }

    public void setAudio_lesson(String audio_lesson) {
        this.audio_lesson = audio_lesson;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getColor_code() {
        return color_code;
    }

    public void setColor_code(String color_code) {
        this.color_code = color_code;
    }

    public String getIs_fav() {
        return is_fav;
    }

    public void setIs_fav(String is_fav) {
        this.is_fav = is_fav;
    }

    public String getShow_at() {
        return show_at;
    }

    public void setShow_at(String show_at) {
        this.show_at = show_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

