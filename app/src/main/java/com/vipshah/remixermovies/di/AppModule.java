package com.vipshah.remixermovies.di;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vipshah.remixermovies.RemixerApp;

import dagger.Module;
import dagger.Provides;

@Module
class AppModule {

    @Provides
    Context provideContext(RemixerApp application) {
        return application.getApplicationContext();
    }

    @Provides
    FirebaseFirestore provideFirebaseFirestore() {
        return FirebaseFirestore.getInstance();
    }

    @Provides
    FirebaseUser provideUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
}
