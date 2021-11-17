package com.hms.grocy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity {

    EditText et_name;
    EditText et_email;
    EditText et_phone;
    EditText et_password;
    EditText et_confirm;
    CheckBox chk_terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_phone = findViewById(R.id.et_phone);
        et_password = findViewById(R.id.et_password);
        et_confirm = findViewById(R.id.et_confirm);
        chk_terms = findViewById(R.id.chk_terms);

        listener();
    }

    public void doSignUp(View view) {
        if(isValid()) {
            Log.d("Grocy", "Sign Up Success");
        }
        else {
            Log.d("Grocy", "Sign Up Failed");
        }
    }

    public boolean isValid() {
        return (isNameValid() && isEmailValid() && isPhoneValid() && isPasswordValid() && isConfirmValid() && isTermsChecked());
    }

    public boolean isTermsChecked() {
        TextView tv_terms = findViewById(R.id.tv_terms);
        if(!chk_terms.isChecked()) {
            tv_terms.setError("Please accept the terms and conditions");
            return false;
        }
        return true;
    }

    public boolean isConfirmValid() {
        String password = et_password.getText().toString();
        String confirm = et_confirm.getText().toString();

        if(confirm.length() == 0) {
            et_confirm.setError("Password should not be empty");
            return false;
        }
        else if(confirm.length() < 8) {
            et_confirm.setError("Password should be more than 8 characters");
            return false;
        }
        else if(!confirm.equals(password)) {
            et_confirm.setError("Confirm password should be same as password");
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

    public boolean isPhoneValid() {
        String phone = et_phone.getText().toString();
        if(phone.length() < 0) {
            et_phone.setError("Phone Number should not be empty");
            return false;
        }

        return true;
    }

    public boolean isEmailValid() {
        String email = et_email.getText().toString().trim();
        if(email.length() == 0) {
            et_email.setError("Email should not be empty");
            return false;
        }

        return true;
    }

    public boolean isNameValid() {
        String name = et_name.getText().toString().trim();
        if(name.length() == 0) {
            et_name.setError("Name should not be empty");
            return false;
        }
        else if(name.length() < 3) {
            et_name.setError("Name should be more than 3 characters");
            return false;
        }

        return true;
    }

    public void listener() {
        et_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    isNameValid();
                }
            }
        });
        et_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    isEmailValid();
                }
            }
        });
        et_phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    isPhoneValid();
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
        et_confirm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    isConfirmValid();
                }
            }
        });
    }
}