package com.example.fyp.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.fyp.API.API_Interface;
import com.example.fyp.Models.Notification;
import com.example.fyp.Models.NotificationResponse;
import com.example.fyp.Models.UserRegistration;
import com.example.fyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class profileact2 extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button logoutButton, needButton;
    private API_Interface apiInterface;
    FirebaseUser user;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileact2);

        logoutButton = findViewById(R.id.logout);
        needButton = findViewById(R.id.need);

        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseMessaging.getInstance().subscribeToTopic("updates");

        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, loginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(profileact2.this, loginActivity.class));
            }
        });

        needButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createJsonToSend();
            }
        });


    }

    private void createJsonToSend(){

        final List<String> device_token = new ArrayList<>();

        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot u : dataSnapshot.getChildren()){

                    String token = u.child("token").getValue(String.class);
                    device_token.add(token);
                    Log.d("adil_token", token);

                }
                user = FirebaseAuth.getInstance().getCurrentUser();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                Log.d("adil", "onDataChange: " + userId);

                //  String userId = "";
                databaseReference.child(userId).addListenerForSingleValueEvent(new  ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserRegistration user = dataSnapshot.getValue(UserRegistration.class);

                        Notification notification = new Notification(
                                "Urgent Blood Required - " + user.getFname() ,
                                "Firstname: " + user.getFname() + ", Blood Group: " + user.getBlood(),
                                device_token);

                        Gson gson = new Gson();

                        try {
                            sendNotification(gson.toJson(notification));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendNotification(String jsonToSend) throws JSONException {



        final RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonToSend)).toString());

        Call<NotificationResponse> Call = apiInterface.send_notification(body);

        Log.d("adil", "sendNotification: " + FirebaseInstanceId.getInstance().getToken());

        Call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {

                Toast.makeText(profileact2.this, "Notifications Send.", Toast.LENGTH_SHORT).show();

                if (response.isSuccessful()) {

                    NotificationResponse notificationResponse = response.body();
                    Log.d("adil", "onResponse: " + notificationResponse);

                    Toast.makeText(profileact2.this, "Notifications Send.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                call.cancel();

                if (t.getMessage().contains("Failed")) {
                    Toast.makeText(profileact2.this, "Kindly check your internet connection.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}