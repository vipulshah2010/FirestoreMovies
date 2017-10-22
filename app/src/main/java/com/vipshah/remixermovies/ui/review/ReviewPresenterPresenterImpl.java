package com.vipshah.remixermovies.ui.review;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.vipshah.remixermovies.RemixConstants;
import com.vipshah.remixermovies.models.RemixMovie;
import com.vipshah.remixermovies.models.RemixMovieReview;

public class ReviewPresenterPresenterImpl implements ReviewContract.ReviewPresenter {

    private ReviewContract.ReviewView reviewView;

    ReviewPresenterPresenterImpl(ReviewContract.ReviewView reviewView) {
        this.reviewView = reviewView;
    }

    @Override
    public void submitReview(final String movieDocumentId, final RemixMovieReview review) {
        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        final DocumentReference movieReference = firestore.collection(RemixConstants.COLLECTION_MOVIES)
                .document(movieDocumentId);
        final DocumentReference reviewReference = firestore
                .collection(RemixConstants.COLLECTION_MOVIES)
                .document(movieDocumentId)
                .collection(RemixConstants.COLLECTION_REVIEWS)
                .document();

        firestore.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                RemixMovie movie = transaction.get(movieReference).toObject(RemixMovie.class);

                movie.setTotalReviews(movie.getTotalReviews() + 1);
                transaction.set(movieReference, movie);
                transaction.set(reviewReference, review);
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reviewView.onSubmitReview(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                reviewView.onSubmitReview(false);
            }
        });
    }

    @Override
    public void deleteReview(final String movieDocumentId, final String reviewId) {
        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                final DocumentReference movieReference = firestore.collection(RemixConstants.COLLECTION_MOVIES)
                        .document(movieDocumentId);
                final DocumentReference reviewReference = firestore
                        .collection(RemixConstants.COLLECTION_MOVIES)
                        .document(movieDocumentId)
                        .collection(RemixConstants.COLLECTION_REVIEWS)
                        .document(reviewId);

                RemixMovie movie = transaction.get(movieReference).toObject(RemixMovie.class);
                movie.setTotalReviews(movie.getTotalReviews() - 1);

                transaction.delete(reviewReference);
                transaction.set(movieReference, movie);

                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reviewView.onDeleteReview(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                reviewView.onDeleteReview(false);
            }
        });
    }
}
