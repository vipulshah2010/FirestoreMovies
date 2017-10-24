package com.vipshah.remixermovies.ui.login;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vipshah.remixermovies.ui.CommonPresenter;

import javax.inject.Inject;

import dagger.Lazy;

public class LoginPresenterImpl<V extends LoginContract.LoginView> extends CommonPresenter<V>
        implements LoginContract.LoginPresenter<V> {

    private FirebaseUser mFirebaseUser;

    @Inject
    LoginPresenterImpl(Lazy<FirebaseUser> firebaseUser) {
        mFirebaseUser = firebaseUser.get();
    }

    @Override
    public void checkLogin() {
        getView().onCheckLogin(mFirebaseUser != null);
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
                            getView().onLoginResult(true);
                        } else {
                            getView().onLoginResult(false);
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
                            getView().onRegisterResult(true);
                        } else {
                            getView().onRegisterResult(false);
                        }
                    }
                });
    }
}
