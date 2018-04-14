package com.nikitagordia.chatme.module.chat.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nikitagordia on 4/3/18.
 */

public class Message {

    private String content;

    private String owner_id;

    private String owner_nickname;

    private String owner_photo_url;

    private String date;

    public Message() {

    }

    public Message(String owner_id, String owner_nickname, String content, String owner_photo_url) {
        this.owner_nickname = owner_nickname;
        this.content = content;
        this.owner_id = owner_id;
        this.owner_photo_url = owner_photo_url;

        date = new SimpleDateFormat("kk:mm").format(new Date()).toString();
    }

    public String getOwner_photo_url() {
        return owner_photo_url;
    }

    public void setOwner_photo_url(String owner_photo_url) {
        this.owner_photo_url = owner_photo_url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getOwner_nickname() {
        return owner_nickname;
    }

    public void setOwner_nickname(String owner_nickname) {
        this.owner_nickname = owner_nickname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
