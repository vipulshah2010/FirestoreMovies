package com.vipshah.remixermovies.ui.movies.list;

import android.content.Context;

import com.google.firebase.firestore.FirebaseFirestore;
import com.vipshah.remixermovies.ui.CommonPresenter;

import javax.inject.Inject;

public class MoviesListPresenterImpl<V extends MoviesListContract.MoviesListView> extends CommonPresenter<V>
        implements MoviesListContract.MoviesListPresenter<V> {

    private Context mContext;
    private FirebaseFirestore mFirebaseFirestore;

    @Inject
    MoviesListPresenterImpl(Context context, FirebaseFirestore firebaseFirestore) {
        mContext = context;
        mFirebaseFirestore = firebaseFirestore;
    }

    @Override
    public void fetchMovies() {
        // TODO - Step-1 Register for Live data updates for Movie Listing
    }

    @Override
    public void uploadMovies() {
        // TODO - Step-2 Upload Movies to Firestore
    }
}
