package com.hms.grocy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class SignInActivity extends AppCompatActivity {

    EditText et_email;
    EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);

        listener();

    }

    public void goToForgotPassword(View view) {
        startActivity(new Intent(SignInActivity.this, ForgotPasswordActivity.class));
    }

    public void goToSignUp(View view) {
        startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
    }

    public void doSignIn(View view) {
        if(isValid()) {
            Log.d("Grocy", "Sign In Success");
        }
        else {
            Log.d("Grocy", "Sign In Failed");
        }
    }

    public boolean isValid() {
        return (isEmailValid() && isPasswordValid());
    }

    public boolean isEmailValid() {
        String email = et_email.getText().toString().trim();
        if(email.length() == 0) {
            et_email.setError("Email should not be empty");
            return false;
        }

        return true;
    }

    public boolean isPasswordValid() {
        String password = et_password.getText().toString();
        if(password.length() == 0) {
            et_password.setError("Password should not be empty");
            return false;
        }
        else if(password.length() < 8) {
            et_password.setError("Password should be more than 8 characters");
            return false;
        }

        return true;
    }

    public void listener() {
        et_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    isEmailValid();
                }
            }
        });
        et_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    isPasswordValid();
                }
            }
        });

    }
}