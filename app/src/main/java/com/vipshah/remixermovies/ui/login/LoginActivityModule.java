package com.vipshah.remixermovies.ui.login;

import dagger.Binds;
import dagger.Module;

import static com.vipshah.remixermovies.ui.login.LoginContract.LoginPresenter;
import static com.vipshah.remixermovies.ui.login.LoginContract.LoginView;

@Module
public abstract class LoginActivityModule {

    @Binds
    abstract LoginPresenter<LoginView> providePresenter(LoginPresenterImpl<LoginView> presenter);
}
