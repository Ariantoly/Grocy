package com.hms.grocy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.feature.service.AuthService;

public class ProfileFragment extends Fragment {

    private AuthAccount authAccount;
    private AccountAuthParams mAuthParam;
    private AuthService mAuthService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, null);

        view.findViewById(R.id.HuaweiIdSignOutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSignOut();
            }
        });

        authAccount = getActivity().getIntent().getParcelableExtra("authAccount");

        return view;
    }

    private void doSignOut() {
        mAuthParam = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setEmail()
                .setAuthorizationCode()
                .createParams();

        // Use AccountAuthParams to build AccountAuthService.
        mAuthService = AccountAuthManager.getService(getContext(), mAuthParam);
        Task<Void> signOutTask = mAuthService.signOut();
        signOutTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("TAG", "signOut Success");
                startActivity(new Intent(getActivity(), SignInActivity.class));
                getActivity().finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.i("TAG", "signOut fail");
            }
        });
    }
}
