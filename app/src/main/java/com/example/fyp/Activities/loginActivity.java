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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class loginActivity extends AppCompatActivity  implements View.OnClickListener{
    private TextView ulogin;
    private EditText ueid;
    private  EditText upassword;
    private TextView signup;
    private Button usubmit;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth= FirebaseAuth.getInstance();
        ulogin = findViewById(R.id.ulogin);
        signup = findViewById(R.id.signup);
        usubmit = findViewById(R.id.loginsubmit);
        ueid = findViewById(R.id.ueid);
        upassword = findViewById(R.id.upassword);


        usubmit.setOnClickListener(this);
        signup.setOnClickListener(this);
    }
    private void userlogin(){
        String email   = ueid.getText().toString();
        String password = upassword.getText().toString();


        if(TextUtils.isEmpty(email)){
            //is empty
            Toast.makeText(this,"please enter email address",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            //is empty
            Toast.makeText(this,"please enter password",Toast.LENGTH_SHORT).show();
            return;
        }



        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener
                (this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(loginActivity.this,"Login Successful" , Toast.LENGTH_SHORT).show();
                            String user_id = firebaseAuth.getCurrentUser().getUid();

                            databaseReference.child(user_id).child("token").setValue(FirebaseInstanceId.getInstance().getToken());

                            Intent i = new Intent (loginActivity.this,profileact2.class);
                            i.putExtra("email", firebaseAuth.getCurrentUser().getEmail());
                            startActivity(i);

                    }
                    else{
                            String s = "Sign up Failed" + task.getException();
                            Toast.makeText(loginActivity.this, s,
                                    Toast.LENGTH_SHORT).show();
                        }
                }});


    }
    @Override
    public void onClick(View view) {
        if (view==usubmit) {

            userlogin();


        }
if (view==signup){
    finish();
    startActivity(new Intent(this, MainActivity.class));

}




    }
}
