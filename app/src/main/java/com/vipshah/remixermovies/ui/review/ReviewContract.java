package com.vipshah.remixermovies.ui.review;

import com.vipshah.remixermovies.models.RemixMovieReview;

class ReviewContract {

    public interface ReviewView {
        void onSubmitReview(boolean success);

        void onDeleteReview(boolean success);
    }

    public interface ReviewPresenter {
        void submitReview(String movieDocumentId, RemixMovieReview review);

        void deleteReview(String movieDocumentId, String reviewId);
    }
}
