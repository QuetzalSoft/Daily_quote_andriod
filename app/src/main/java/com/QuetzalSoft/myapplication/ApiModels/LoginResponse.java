package com.QuetzalSoft.myapplication.ApiModels;

public class LoginResponse {

    loginresponse response;
    boolean status;
    String message;

    public LoginResponse() {
    }

    public loginresponse getResponse() {
        return response;
    }

    public void setResponse(loginresponse response) {
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

    public class loginresponse{
        int id;
        String username;
        String email;
        String password;
        String address;
        String phone_number;

        public loginresponse() {
        }

        public loginresponse(int id, String username, String email, String password, String address, String phone_number) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.password = password;
            this.address = address;
            this.phone_number = phone_number;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public void setPhone_number(String phone_number) {
            this.phone_number = phone_number;
        }
    }
}
