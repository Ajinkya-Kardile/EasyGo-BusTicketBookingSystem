package com.ajinkya.easygo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button btnLogin;
    private TextView RedirectRegister, forgotPassword;
    private EditText et_Email, et_Password;
    private ProgressDialog progressDialog,loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Initialize();
        Buttons();
    }

    private void Initialize() {
        mAuth = FirebaseAuth.getInstance();
        btnLogin = findViewById(R.id.LoginButton);
        et_Email = findViewById(R.id.LogEmailID);
        et_Password = findViewById(R.id.LogPasswd);
        forgotPassword = findViewById(R.id.ForgotPassword);
        RedirectRegister = findViewById(R.id.RedirectRegister);
        progressDialog = new ProgressDialog(this);
    }

    private void Buttons() {
        forgotPassword.setOnClickListener(View -> ShowForgotPassDialog());

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Password = et_Password.getText().toString().trim();
                String Email = et_Email.getText().toString().trim();


                if (!Password.isEmpty() && !Email.isEmpty()) {
                    progressDialog.setTitle("Processing");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    Login(Email, Password);

                } else {
                    Toast.makeText(LoginActivity.this, "Please fill each box", Toast.LENGTH_SHORT).show();
                }


            }
        });

        RedirectRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);

            }
        });

    }


    private void ShowForgotPassDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Forgot Password...?").setCancelable(false).setMessage("Please provide your registered email.").setIcon(R.drawable.ic_user);
        LinearLayout linearLayout=new LinearLayout(this);
        final EditText editText= new EditText(this);

        // write the email using which you registered
        editText.setHint("Enter Registered Email");
        editText.setMinEms(16);
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(editText);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);


        // Click on Recover and a email will be sent to your registered email id
        builder.setPositiveButton("Reset Password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = editText.getText().toString().trim();
                if(!email.isEmpty()){
                    beginRecovery(email);
                }else{
//                    editText.setError("Enter Email");
                    Toast.makeText(LoginActivity.this, "Sorry, You Not provided Email", Toast.LENGTH_LONG).show();
                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void beginRecovery(String email) {
        loadingBar=new ProgressDialog(this);
        loadingBar.setMessage("Sending Email....");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();


        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loadingBar.dismiss();
                if(task.isSuccessful())
                {
                    Toast.makeText(LoginActivity.this,"Email Sent successfully...",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(LoginActivity.this,"Error Occurred",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingBar.dismiss();
                Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void Login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()) {
                        progressDialog.dismiss();
                        Intent in = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(in);
                        finish();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Please verify your email", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Wrong login credentials", Toast.LENGTH_SHORT).show();

                }

            }
        });


    }
}