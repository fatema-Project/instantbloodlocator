package com.example.fyp.Models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notification {

    @SerializedName("title")

    private String title;
    @SerializedName("msg")

    private String msg;
    @SerializedName("device_token")

    private List<String> deviceToken = null;

    public Notification(String title, String msg, List<String> deviceToken) {
        this.title = title;
        this.msg = msg;
        this.deviceToken = deviceToken;
    }
}