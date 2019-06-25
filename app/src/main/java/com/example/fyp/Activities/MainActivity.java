package com.example.fyp.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText EdittextEMAIL;
    private EditText EdittextPASSWORD;
    private Button submit;
    private TextView login;


private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

firebaseAuth=FirebaseAuth.getInstance();



        TextView regi = (TextView) findViewById(R.id.registration);
        login = (TextView) findViewById(R.id.login);
        submit = (Button) findViewById(R.id.submit);
        EdittextEMAIL = findViewById(R.id.eid);
        EdittextPASSWORD = findViewById(R.id.password);

        submit.setOnClickListener(this);
        login.setOnClickListener(this);

    }
    private void RrgisterUser(){

        String EMAIL=EdittextEMAIL.getText().toString();
        String PASSWORD=EdittextPASSWORD.getText().toString();

        if(TextUtils.isEmpty(EMAIL)){
            //is empty
            Toast.makeText(this,"please enter email address",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(PASSWORD)){
            //is empty
            Toast.makeText(this,"please enter password",Toast.LENGTH_SHORT).show();
            return;
        }


        firebaseAuth.createUserWithEmailAndPassword(EMAIL,PASSWORD)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        Toast.makeText(MainActivity.this,"REGISTERED SUCCESSFULLY",Toast.LENGTH_SHORT)
                                .show();
                        finish();

                        startActivity(new Intent(getApplicationContext(), profileAct.class));

                    }
                    else{
                        Toast.makeText(MainActivity.this,"something went wrong",Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                }
            });

    }

    @Override
    public void onClick(View view) {

        if(view==submit){

            RrgisterUser();


        }

if (view==login){

    startActivity(new Intent(this,loginActivity.class));

    //login activity
}


    }

}
