package com.nikitagordia.chatme.module.main.profile.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nikitagordia on 3/28/18.
 */

public class BlogPost {

    private String id;

    private String owner_id;

    private String owner_name;

    private String owner_photo_url;

    private String date;

    private String content;

    private int view, comment, like;

    public BlogPost() {}

    public BlogPost(String content, String id, String owner_id, String owner_name) {
        view = comment = like = 0;
        this.content = content;
        this.id = id;
        this.owner_id = owner_id;
        this.owner_name = owner_name;
        date = new SimpleDateFormat("EEE, d MMM yyyy").format(new Date()).toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getOwner_photo_url() {
        return owner_photo_url;
    }

    public void setOwner_photo_url(String owner_photo_url) {
        this.owner_photo_url = owner_photo_url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }
}
