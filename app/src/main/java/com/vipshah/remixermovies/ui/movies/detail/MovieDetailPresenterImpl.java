package com.vipshah.remixermovies.ui.movies.detail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.vipshah.remixermovies.RemixConstants;
import com.vipshah.remixermovies.models.RemixMovie;
import com.vipshah.remixermovies.models.RemixMovieRating;
import com.vipshah.remixermovies.ui.CommonPresenter;

import javax.inject.Inject;

import dagger.Lazy;

public class MovieDetailPresenterImpl<V extends MovieDetailContract.MovieDetailView> extends CommonPresenter<V>
        implements MovieDetailContract.MovieDetailPresenter<V> {

    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseUser mFirebaseUser;

    @Inject
    MovieDetailPresenterImpl(FirebaseFirestore firebaseFirestore, Lazy<FirebaseUser> firebaseUser) {
        mFirebaseFirestore = firebaseFirestore;
        mFirebaseUser = firebaseUser.get();
    }

    @Override
    public void loadRatings(String movieDocumentId) {
        Query query = mFirebaseFirestore
                .collection(RemixConstants.COLLECTION_MOVIES)
                .document(movieDocumentId)
                .collection(RemixConstants.COLLECTION_RATINGS)
                .whereEqualTo("email", mFirebaseUser.getEmail());

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                if (documentSnapshots.isEmpty()) {
                    getView().onLoadRatings(true, 0);
                } else {
                    RemixMovieRating movieRating = documentSnapshots.getDocuments().get(0).toObject(RemixMovieRating.class);
                    getView().onLoadRatings(true, movieRating.getRatings());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                getView().onLoadRatings(false, 0);
            }
        });
    }

    @Override
    public void submitRatings(final RemixMovieRating remixMovieRating, final String documentId) {

        mFirebaseFirestore.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentReference movieReference = mFirebaseFirestore
                        .collection(RemixConstants.COLLECTION_MOVIES)
                        .document(documentId);
                DocumentReference ratingReference = movieReference.collection(RemixConstants.COLLECTION_RATINGS)
                        .document(mFirebaseUser.getUid());

                RemixMovie movie = transaction.get(movieReference).toObject(RemixMovie.class);

                // Compute new number of ratings
                int newNumRatings = movie.getTotalRatings() + 1;

                // Compute new average rating
                double oldRatingTotal = movie.getAverageRatings() * movie.getTotalRatings();
                double newAvgRating = (oldRatingTotal + remixMovieRating.getRatings()) / newNumRatings;

                // Set new restaurant info
                movie.setTotalRatings(newNumRatings);
                movie.setAverageRatings((float) newAvgRating);

                transaction.set(ratingReference, remixMovieRating);
                transaction.set(movieReference, movie);

                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                getView().onSubmitRatings(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                getView().onSubmitRatings(false);
            }
        });
    }
}
