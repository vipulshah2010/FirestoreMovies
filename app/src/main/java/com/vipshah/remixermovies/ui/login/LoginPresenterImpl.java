package com.vipshah.remixermovies.ui.login;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPresenterImpl implements LoginContract.LoginPresenter {

    private LoginContract.LoginView loginView;

    LoginPresenterImpl(LoginContract.LoginView loginView) {
        this.loginView = loginView;
    }

    @Override
    public void checkLogin() {
        loginView.onCheckLogin(FirebaseAuth.getInstance().getCurrentUser() != null);
    }

    @Override
    public void login(String username, String password) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            return;
        }
        FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            loginView.onLoginResult(true);
                        } else {
                            loginView.onLoginResult(false);
                        }
                    }
                });
    }

    @Override
    public void register(String username, String password) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            return;
        }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            loginView.onRegisterResult(true);
                        } else {
                            loginView.onRegisterResult(false);
                        }
                    }
                });
    }
}
