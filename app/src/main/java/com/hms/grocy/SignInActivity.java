package com.hms.grocy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.account.service.AccountAuthService;
import com.huawei.hms.support.api.entity.common.CommonConstant;

public class SignInActivity extends AppCompatActivity {

    private AccountAuthService mAuthService;

    // Specify the user information to be obtained after user authorization.
    private AccountAuthParams mAuthParam;

    // Define the request code for signInIntent.
    private static final int REQUEST_CODE_SIGN_IN = 1000;

    // Define the log flag.
    private static final String TAG = "Grocy";

//    Uncomment these if you don't want to log in via Huawei ID
//    private EditText et_email;
//    private EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        findViewById(R.id.HuaweiIdAuthButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInByHwId();
            }
        });

//    Uncomment these if you don't want to log in via Huawei ID
//    et_email = findViewById(R.id.et_email);
//    et_password = findViewById(R.id.et_password);
//    listener();

    }

    public void goToMainPage(AuthAccount authAccount) {
        startActivity(new Intent(SignInActivity.this, MainActivity.class).putExtra("authAccount", authAccount));
        finish();
    }

//    Uncomment these if you don't want to log in via Huawei ID
//    public void doSignIn(View view) {
//        if(isValid()) {
//            Log.d(TAG, "Sign In Success");
//        }
//        else {
//            Log.d(TAG, "Sign In Failed");
//        }
//    }
//
//    public boolean isValid() {
//        return (isEmailValid() && isPasswordValid());
//    }
//
//    public boolean isEmailValid() {
//        String email = et_email.getText().toString().trim();
//        if(email.length() == 0) {
//            et_email.setError("Email should not be empty");
//            return false;
//        }
//
//        return true;
//    }
//
//    public boolean isPasswordValid() {
//        String password = et_password.getText().toString();
//        if(password.length() == 0) {
//            et_password.setError("Password should not be empty");
//            return false;
//        }
//        else if(password.length() < 8) {
//            et_password.setError("Password should be more than 8 characters");
//            return false;
//        }
//
//        return true;
//    }
//
//    public void listener() {
//        et_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(!hasFocus) {
//                    isEmailValid();
//                }
//            }
//        });
//        et_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(!hasFocus) {
//                    isPasswordValid();
//                }
//            }
//        });
//
//    }

    private void signInByHwId() {
        //  1. Use AccountAuthParams to specify the user information to be obtained after user authorization, including the user ID (OpenID and UnionID), email address, and profile (nickname and picture).
        // 2. By default, DEFAULT_AUTH_REQUEST_PARAM specifies two items to be obtained, that is, the user ID and profile.
        // 3. If your app needs to obtain the user's email address, call setEmail().
        // 4. To support authorization code-based HUAWEI ID sign-in, use setAuthorizationCode(). All user information that your app is authorized to access can be obtained through the relevant API provided by the Account Kit server.
        mAuthParam = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setEmail()
                .setAuthorizationCode()
                .createParams();

        // Use AccountAuthParams to build AccountAuthService.
        mAuthService = AccountAuthManager.getService(this, mAuthParam);
        Intent signInIntent = mAuthService.getSignInIntent();
        // If your app appears in full screen mode when a user tries to sign in, that is, with no status bar at the top of the device screen, add the following parameter in the intent:
        // intent.putExtra(CommonConstant.RequestParams.IS_FULL_SCREEN, true);
        // Check the details in this FAQ.
        signInIntent.putExtra(CommonConstant.RequestParams.IS_FULL_SCREEN, true);
        startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN);

        // Sign in with a HUAWEI ID silently.
        Task<AuthAccount> task = mAuthService.silentSignIn();
        task.addOnSuccessListener(new OnSuccessListener<AuthAccount>() {
            @Override
            public void onSuccess(AuthAccount authAccount) {
                // The silent sign-in is successful. Process the returned AuthAccount object to obtain the HUAWEI ID information.
                goToMainPage(authAccount);
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // The silent sign-in fails. Your app will call getSignInIntent() to show the authorization or sign-in screen.
                if (e instanceof ApiException) {
//                    progressDialog.dismiss();
                    ApiException apiException = (ApiException) e;
                    Intent signInIntent = mAuthService.getSignInIntent();
                    // If your app appears in full screen mode when a user tries to sign in, that is, with no status bar at the top of the device screen, add the following parameter in the intent:
                    // intent.putExtra(CommonConstant.RequestParams.IS_FULL_SCREEN, true);
                    // Check the details in this FAQ.
                    signInIntent.putExtra(CommonConstant.RequestParams.IS_FULL_SCREEN, true);
                    startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            Log.i(TAG, "onActivitResult of sigInInIntent, request code: " + REQUEST_CODE_SIGN_IN);
            Task<AuthAccount> authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data);
            if (authAccountTask.isSuccessful()) {
                // The sign-in is successful, and the authAccount object that contains the HUAWEI ID information is obtained.
                AuthAccount authAccount = authAccountTask.getResult();
                Log.i(TAG, "onActivitResult of sigInInIntent, request code: " + REQUEST_CODE_SIGN_IN);
            } else {
                // The sign-in fails. Find the cause from the status code. For more information, please refer to Error Codes.
                Log.e(TAG, "sign in failed : " +((ApiException)authAccountTask.getException()).getStatusCode());
            }
        }
    }
}