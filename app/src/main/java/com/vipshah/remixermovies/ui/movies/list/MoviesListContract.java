package com.vipshah.remixermovies.ui.movies.list;

import android.content.Context;

import com.google.firebase.firestore.Query;
import com.vipshah.remixermovies.ui.BasePresenter;
import com.vipshah.remixermovies.ui.BaseView;

class MoviesListContract {

    public interface MoviesListView extends BaseView {
        void onFetchMovies(Query query);

        void onUploadMovies(boolean success);
    }

    public interface MoviesListPresenter<V extends MoviesListView> extends BasePresenter<V> {
        void fetchMovies();

        void uploadMovies();
    }
}
