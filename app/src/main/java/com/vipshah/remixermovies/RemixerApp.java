package com.vipshah.remixermovies;

import com.google.android.libraries.remixer.Remixer;
import com.google.android.libraries.remixer.storage.LocalStorage;
import com.google.android.libraries.remixer.ui.RemixerInitialization;
import com.vipshah.remixermovies.di.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class RemixerApp extends DaggerApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        RemixerInitialization.initRemixer(this);
        Remixer.getInstance().setSynchronizationMechanism(new LocalStorage(getApplicationContext()));
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().create(this);
    }
}
