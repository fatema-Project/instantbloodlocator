package com.example.fyp.Models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notification {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("device_token")
    @Expose
    private List<String> deviceToken = null;

    public Notification(String title, String msg, List<String> deviceToken) {
        this.title = title;
        this.msg = msg;
        this.deviceToken = deviceToken;
    }
}