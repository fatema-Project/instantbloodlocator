package com.example.fyp.API;

import com.example.fyp.Models.NotificationResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface API_Interface {

    @POST("Api/AttendanceManager/public/send_notification")
    Call<NotificationResponse> send_notification(
            @Body RequestBody jsonBody
    );

}