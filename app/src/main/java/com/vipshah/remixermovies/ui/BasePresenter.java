package com.vipshah.remixermovies.ui;

public interface BasePresenter<V extends BaseView> {

    void attach(V view);

    void detach();
}
