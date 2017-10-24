package com.vipshah.remixermovies.di;

import com.vipshah.remixermovies.RemixerApp;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        ActivityBindingModule.class,
        AppModule.class,
})
public interface AppComponent extends AndroidInjector<RemixerApp> {

    void inject(RemixerApp application);

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<RemixerApp> {
    }
}
