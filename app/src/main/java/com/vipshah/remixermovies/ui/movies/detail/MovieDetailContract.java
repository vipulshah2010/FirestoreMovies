package com.vipshah.remixermovies.ui.movies.detail;

import com.vipshah.remixermovies.models.RemixMovieRating;
import com.vipshah.remixermovies.ui.BasePresenter;
import com.vipshah.remixermovies.ui.BaseView;

class MovieDetailContract {

    public interface MovieDetailView extends BaseView {
        void onLoadRatingsSuccess(float ratings);

        void onLoadRatingsFailed();

        void onSubmitRatings();

        void onSubmitRatingsFailed();
    }

    public interface MovieDetailPresenter<V extends MovieDetailView> extends BasePresenter<V> {
        void loadRatings(String movieDocumentId);

        void submitRatings(RemixMovieRating ratings, String movieDocumentId);
    }
}
