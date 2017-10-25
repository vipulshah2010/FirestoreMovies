package com.vipshah.remixermovies.ui.review;

import com.google.firebase.firestore.FirebaseFirestore;
import com.vipshah.remixermovies.models.RemixMovieReview;
import com.vipshah.remixermovies.ui.CommonPresenter;

import javax.inject.Inject;

public class ReviewPresenterImpl<V extends ReviewContract.ReviewView> extends CommonPresenter<V>
        implements ReviewContract.ReviewPresenter<V> {

    private FirebaseFirestore mFirebaseFirestore;

    @Inject
    ReviewPresenterImpl(FirebaseFirestore firebaseFirestore) {
        mFirebaseFirestore = firebaseFirestore;
    }

    @Override
    public void submitReview(final String movieDocumentId, final RemixMovieReview review) {

        // TODO - Step-7 Submit Review and call following

        getView().onSubmitReview(true);
    }

    @Override
    public void deleteReview(final String movieDocumentId, final String reviewId) {

        // TODO - Step-8 Delete Review and call following

        getView().onDeleteReview(true);
    }
}
