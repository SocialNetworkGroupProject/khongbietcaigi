package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText edtMail;
    private EditText edtPassword;
    private ProgressBar pgbLoading;
    private ImageView imgRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mapping();
        imgRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                btnLogin.setVisibility(View.INVISIBLE);
                pgbLoading.setVisibility(View.VISIBLE);
                String mail = edtMail.getText().toString();
                String password = edtPassword.getText().toString();
                if(mail.isEmpty() || password.isEmpty()) {
                    btnLogin.setVisibility(View.VISIBLE);
                    pgbLoading.setVisibility((View.INVISIBLE));
                    sendMessage("Please!");
                } else {
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(LoginActivity.this,
                            new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                updateUI();
                            } else {
                                sendMessage("False" + task.getException().getMessage());
                                btnLogin.setVisibility(View.VISIBLE);
                                pgbLoading.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            updateUI();
            finish();
        }
    }

    private void updateUI() {

        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();

    }

    private void sendMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void mapping() {
        btnLogin = findViewById(R.id.login_btn);
        edtMail = findViewById(R.id.login_mail);
        edtPassword = findViewById(R.id.login_password);
        pgbLoading = findViewById(R.id.login_pgb);
        imgRegister = findViewById(R.id.login_image);
    }
}