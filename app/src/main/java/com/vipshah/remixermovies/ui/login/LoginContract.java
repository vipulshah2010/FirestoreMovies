package com.vipshah.remixermovies.ui.login;

import com.vipshah.remixermovies.ui.BasePresenter;
import com.vipshah.remixermovies.ui.BaseView;

class LoginContract {

    public interface LoginView extends BaseView {
        void onCheckLogin(boolean isLoggedIn);

        void onLoginResult(boolean isSuccess);

        void onRegisterResult(boolean isSuccess);
    }

    public interface LoginPresenter<V extends LoginView> extends BasePresenter<V> {
        void checkLogin();

        void login(String username, String password);

        void register(String username, String password);
    }
}
