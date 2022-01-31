package com.hms.grocy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hms.grocy.MainActivity;
import com.hms.grocy.R;
import com.hms.grocy.SignInActivity;
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

    private TextView tvName, tvEmail, tvLocation;
    private ImageView imgUser;

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

        authAccount = ((MainActivity) getActivity()).getAuthAccount();
        tvName = view.findViewById(R.id.tv_name);
        tvEmail = view.findViewById(R.id.tv_email);
        tvLocation = view.findViewById(R.id.tv_location);
        tvLocation.setText(((MainActivity) getActivity()).getCurrentLocation());
        imgUser = view.findViewById(R.id.img_user);

        tvName.setText(authAccount.getDisplayName());
        tvEmail.setText(authAccount.getEmail());
        if(!authAccount.getAvatarUri().toString().equals(""))
            imgUser.setImageURI(authAccount.getAvatarUri());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        tvLocation.setText(((MainActivity) getActivity()).getCurrentLocation());
        if(((MainActivity) getActivity()).getCurrentLocation().equals("")) {
            tvLocation.setVisibility(View.INVISIBLE);
        }
        else
            tvLocation.setVisibility(View.VISIBLE);
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
