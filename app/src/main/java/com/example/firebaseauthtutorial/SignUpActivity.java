package com.example.firebaseauthtutorial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class SignUpActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    Button btnSignUp;
    // firebase authentication object;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // direct to home screen if the user have already signed in
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
            finish();
        }

        // when the user have account and clicks on log in
        TextView txtView_login;
        txtView_login = findViewById(R.id.txtView_login);
        txtView_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // get details
        edtUsername = findViewById(R.id.edtUsername_of_signup);
        edtPassword = findViewById(R.id.edtPassword_of_signup);
        btnSignUp = findViewById(R.id.btnSignUp); // Initialize btnSignUp

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                if (username.isEmpty()) {
                    edtUsername.setError("Username required");
                    edtUsername.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    edtPassword.setError("Password required");
                    edtPassword.requestFocus();
                    return;
                }
                if (password.length() < 6) {
                    edtPassword.setError("Password should have minimum 6 characters");
                    edtPassword.requestFocus();
                    return;
                }

                signUpUser(username, password);
            }
        });
    }

    void signUpUser(String username, String password) {
        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // redirect to main activity
                        startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        // show error message
                        Exception exception = task.getException();
                        if (exception instanceof FirebaseAuthException) {
                            String errorCode = ((FirebaseAuthException) exception).getErrorCode();
                            if (errorCode.equals("ERROR_EMAIL_ALREADY_IN_USE")) {
                                edtUsername.setError("Email already in use");
                                edtUsername.requestFocus();
                            } else {
                                Toast.makeText(SignUpActivity.this, "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignUpActivity.this, "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}