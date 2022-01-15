package com.example.werewolfkiller;

import androidx.lifecycle.ViewModel;

import com.example.werewolfkiller.modle.User;

public class MyViewModle extends ViewModel {
    private User user;
    private String token;
    private String registered;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }
}

