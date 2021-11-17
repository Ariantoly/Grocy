package com.hms.grocy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ForgotPasswordOtpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_otp);
    }

    public void doConfirm(View view) {
        startActivity(new Intent(ForgotPasswordOtpActivity.this, ChangePasswordActivity.class));
    }
}