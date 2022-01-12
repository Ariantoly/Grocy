package com.hms.grocy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

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

public class SplashActivity extends AppCompatActivity {

    private AccountAuthService mAuthService;

    // Specify the user information to be obtained after user authorization.
    private AccountAuthParams mAuthParam;

    // Define the request code for signInIntent.
    private static final int REQUEST_CODE_SIGN_IN = 1000;

    // Define the log flag.
    private static final String TAG = "Grocy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                silentSignInByHwId();
            }
        }, 3000);
    }

    private void silentSignInByHwId() {
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

        // Sign in with a HUAWEI ID silently.
        Task<AuthAccount> task = mAuthService.silentSignIn();
        task.addOnSuccessListener(new OnSuccessListener<AuthAccount>() {
            @Override
            public void onSuccess(AuthAccount authAccount) {
                // The silent sign-in is successful. Process the returned AuthAccount object to obtain the HUAWEI ID information.
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("authAccount", authAccount);
                startActivity(intent);
                finish();
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // The silent sign-in fails. Your app will call getSignInIntent() to show the authorization or sign-in screen.
                if (e instanceof ApiException) {
                    Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

}