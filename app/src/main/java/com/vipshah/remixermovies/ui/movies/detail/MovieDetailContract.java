package com.vipshah.remixermovies.ui.movies.detail;

import com.vipshah.remixermovies.models.RemixMovieRating;

public class MovieDetailContract {

    public interface MovieDetailView {
        void onLoadRatingsSuccess(float ratings);

        void onLoadRatingsFailed();

        void onSubmitRatings();

        void onSubmitRatingsFailed();
    }

    public interface MovieDetailPresenter {
        void loadRatings(String movieDocumentId);

        void submitRatings(RemixMovieRating ratings, String movieDocumentId);
    }
}
