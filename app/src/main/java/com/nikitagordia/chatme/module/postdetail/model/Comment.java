package com.nikitagordia.chatme.module.postdetail.model;

/**
 * Created by nikitagordia on 3/31/18.
 */

public class Comment {

    private String owner_uid;

    private String context;

    private String owner_name;

    public Comment() {}

    public Comment(String owner_name, String context) {
        this.owner_name = owner_name;
        this.context = context;
    }

    public String getOwner_uid() {
        return owner_uid;
    }

    public void setOwner_uid(String owner_uid) {
        this.owner_uid = owner_uid;
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
