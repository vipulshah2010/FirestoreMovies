package com.vipshah.remixermovies.ui;

public class CommonPresenter<V extends BaseView> implements BasePresenter<V> {

    private V view;

    @Override
    public void attach(V view) {
        this.view = view;
    }

    @Override
    public void detach() {
        this.view = null;
    }

    public V getView() {
        return view;
    }
}
