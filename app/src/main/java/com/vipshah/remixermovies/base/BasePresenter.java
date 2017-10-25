package com.vipshah.remixermovies.base;

public interface BasePresenter<V extends BaseView> {

    void attach(V view);

    void detach();
}
