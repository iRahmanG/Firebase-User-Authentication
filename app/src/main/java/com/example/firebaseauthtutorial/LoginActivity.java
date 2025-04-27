package com.example.firebaseauthtutorial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextView txtView_signup;
    FirebaseAuth mAuth;
    EditText edtUsername, edtPassword;
    Button btnLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }

        // when user log in
        edtUsername = findViewById(R.id.edtUsername_of_login);
        edtPassword = findViewById(R.id.edtPassword_of_login);
        btnLogIn = findViewById(R.id.btnLogin); // Initialize btnSignUp

        btnLogIn.setOnClickListener(new View.OnClickListener() {
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

                logInUser(username, password);
            }
        });

        txtView_signup = findViewById(R.id.txtView_signup);
        txtView_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);

            }
        });

    }

    private void logInUser(String username, String password) {

        mAuth.signInWithEmailAndPassword(username, password);
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        } else{
            edtUsername.setError("Invalid credentials");
            edtUsername.requestFocus();
        }

    }
}