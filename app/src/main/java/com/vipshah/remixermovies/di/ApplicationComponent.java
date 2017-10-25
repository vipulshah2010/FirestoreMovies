package com.vipshah.remixermovies.di;

import com.vipshah.remixermovies.app.RemixerApp;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        ActivityBindingModule.class,
        ApplicationModule.class,
})
public interface ApplicationComponent extends AndroidInjector<RemixerApp> {

    void inject(RemixerApp application);

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<RemixerApp> {
    }
}
