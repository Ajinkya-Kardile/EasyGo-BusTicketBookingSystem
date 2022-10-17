package com.ajinkya.easygo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        int SPLASH_SCREEN_TIME_OUT = 2000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if(currentUser ==null){
                    Intent in = new Intent(SplashScreen.this,LoginActivity.class);
                    startActivity(in);
                }else if(currentUser.isEmailVerified()){
                    Intent in = new Intent(SplashScreen.this, HomeActivity.class);
                    startActivity(in);
                }
                finish();
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }
}