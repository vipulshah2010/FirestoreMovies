package com.vipshah.remixermovies.ui.review;

import com.vipshah.remixermovies.models.RemixMovieReview;
import com.vipshah.remixermovies.ui.BasePresenter;
import com.vipshah.remixermovies.ui.BaseView;

class ReviewContract {

    public interface ReviewView extends BaseView {
        void onSubmitReview(boolean success);

        void onDeleteReview(boolean success);
    }

    public interface ReviewPresenter<V extends ReviewView> extends BasePresenter<V> {
        void submitReview(String movieDocumentId, RemixMovieReview review);

        void deleteReview(String movieDocumentId, String reviewId);
    }
}
