package com.vipshah.remixermovies.ui.movies.detail;

import com.vipshah.remixermovies.base.BasePresenter;
import com.vipshah.remixermovies.base.BaseView;
import com.vipshah.remixermovies.models.RemixMovieRating;

class MovieDetailContract {

    public interface MovieDetailView extends BaseView {
        void onLoadRatings(boolean success, float ratings);

        void onSubmitRatings(boolean success);

        void onMovieDeleted(boolean success);
    }

    public interface MovieDetailPresenter<V extends MovieDetailView> extends BasePresenter<V> {
        void loadRatings(String movieDocumentId);

        void submitRatings(RemixMovieRating ratings, String movieDocumentId);

        void deleteMovie(String documentId);
    }
}
