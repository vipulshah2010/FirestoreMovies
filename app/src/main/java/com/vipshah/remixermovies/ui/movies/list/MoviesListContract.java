package com.vipshah.remixermovies.ui.movies.list;

import com.google.firebase.firestore.Query;
import com.vipshah.remixermovies.base.BasePresenter;
import com.vipshah.remixermovies.base.BaseView;

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
