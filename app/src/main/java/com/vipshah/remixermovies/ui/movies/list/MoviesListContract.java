package com.vipshah.remixermovies.ui.movies.list;

import android.content.Context;

import com.google.firebase.firestore.Query;

class MoviesListContract {

    public interface MoviesListView {
        void onFetchMovies(Query query);

        void onUploadMovies(boolean success);
    }

    public interface MoviesListPresenter {
        void fetchMovies();

        void uploadMovies(Context context);
    }
}
