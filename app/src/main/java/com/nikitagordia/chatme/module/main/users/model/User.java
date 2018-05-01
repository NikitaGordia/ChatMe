package com.nikitagordia.chatme.module.main.users.model;

/**
 * Created by nikitagordia on 3/30/18.
 */

public class User {

    private String name;

    private String email;

    private String uid;

    private String photo_url;

    public User() {}

    public User(String uid, String name, String email) {
        this.name = name;
        this.email = email;
    }

    public User(String name) {
        this.name = name;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
