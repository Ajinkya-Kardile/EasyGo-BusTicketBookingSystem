package com.ajinkya.easygo;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private Button RegButton;
    private TextView RedirectBtn;
    private EditText et_Email, et_Password,et_Phone,et_Name;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    private FirebaseUser currentUser;
    private String Phone,Name,Password,Email,confirmPassword;
    private EditText Confirm_Password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Initialize();
        buttons();
    }


    private void Initialize(){
        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();

        et_Name = findViewById(R.id.RegName);
        et_Phone = findViewById(R.id.RegPhoneNo);
        et_Email = findViewById(R.id.RegEmailID);
        et_Password = findViewById(R.id.RegPasswd);
        Confirm_Password  = findViewById(R.id.RegConformPasswd);
        RegButton = findViewById(R.id.RegButton);
        RedirectBtn = findViewById(R.id.RedirectLogin);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

    }


    private void  buttons(){

        RegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email = et_Email.getText().toString().trim();
                Password = et_Password.getText().toString().trim();
                Phone = et_Phone.getText().toString().trim();
                Name = et_Name.getText().toString();
                confirmPassword = Confirm_Password.getText().toString().trim();

                if(confirmPassword.equals(Password)){
                    if(!Phone.isEmpty() && !Password.isEmpty() && !Name.isEmpty()&& !Email.isEmpty()){
                        progressDialog.setTitle("Registering");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        SignIn(Email,Password);
                    }else {
                        Toast.makeText(RegisterActivity.this,"Please fill each box",Toast.LENGTH_SHORT).show();

                    }

                }else{
                    Confirm_Password.setError("Password Do Not Match");
                    Toast.makeText(RegisterActivity.this,"Password do not match",Toast.LENGTH_LONG).show();

                }



            }
        });


        RedirectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }



    private void SignIn(String email, String password){
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Objects.requireNonNull(auth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this,"Please verify your email address",Toast.LENGTH_SHORT).show();
                                RegisterUser(Phone,Name,Email);
                            }else {
                                Toast.makeText(RegisterActivity.this,"Wrong email address",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


                }else {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this,"Please try again",Toast.LENGTH_SHORT).show();

                }

            }
        });


    }
    private void RegisterUser(String Phone,String Name, String Email){
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        String Current_Uid = currentUser.getUid();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Users").child(Current_Uid);
        Log.e(TAG, "RegisterPhone: "+ database);
        HashMap<String, String> user = new HashMap<>();
        user.put("Name",Name);
        user.put("Phone",Phone);
        user.put("Email",Email);
        database.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,"Registered Successfully",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

                else{
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this,"Filed to register info",Toast.LENGTH_SHORT).show();
                }
            }


        });


    }


}