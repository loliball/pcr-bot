package com.cnl.mybot.ys.signin.entity;

import java.util.List;

public class User {
    private String cookie;
    private List<GameRole> role;

    public User(String cookie) {
        this.cookie = cookie;
    }

    public String getCookie() {
        return cookie;
    }

    public List<GameRole> getRole() {
        return role;
    }

    public void setRole(List<GameRole> role) {
        this.role = role;
    }
}
