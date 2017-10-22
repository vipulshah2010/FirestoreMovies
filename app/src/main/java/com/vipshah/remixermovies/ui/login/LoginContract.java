package com.vipshah.remixermovies.ui.login;

class LoginContract {

    public interface LoginView {
        void onCheckLogin(boolean isLoggedIn);

        void onLoginResult(boolean isSuccess);

        void onRegisterResult(boolean isSuccess);
    }

    public interface LoginPresenter {
        void checkLogin();

        void login(String username, String password);

        void register(String username, String password);
    }
}
