package com.nikitagordia.chatme.module.postdetail.model;

/**
 * Created by nikitagordia on 3/31/18.
 */

public class Comment {

    private String owner_id;

    private String context;

    private String owner_name;

    private String owner_photo_url;

    public Comment() {}

    public Comment(String owner_name, String context) {
        this.owner_name = owner_name;
        this.context = context;
    }

    public String getOwner_photo_url() {
        return owner_photo_url;
    }

    public void setOwner_photo_url(String owner_photo_url) {
        this.owner_photo_url = owner_photo_url;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }
}
