package com.vipshah.remixermovies.ui.movies.detail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.vipshah.remixermovies.RemixConstants;
import com.vipshah.remixermovies.models.RemixMovie;
import com.vipshah.remixermovies.models.RemixMovieRating;

public class MovieDetailPresenterImpl implements MovieDetailContract.MovieDetailPresenter {

    private MovieDetailContract.MovieDetailView movieDetailView;

    MovieDetailPresenterImpl(MovieDetailContract.MovieDetailView movieDetailView) {
        this.movieDetailView = movieDetailView;
    }

    @Override
    public void loadRatings(String movieDocumentId) {
        Query query = FirebaseFirestore.getInstance()
                .collection(RemixConstants.COLLECTION_MOVIES)
                .document(movieDocumentId)
                .collection(RemixConstants.COLLECTION_RATINGS)
                .whereEqualTo("email", FirebaseAuth.getInstance().getCurrentUser().getEmail());

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                if (documentSnapshots.isEmpty()) {
                    movieDetailView.onLoadRatingsSuccess(0);
                } else {
                    RemixMovieRating movieRating = documentSnapshots.getDocuments().get(0).toObject(RemixMovieRating.class);
                    movieDetailView.onLoadRatingsSuccess(movieRating.getRatings());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                movieDetailView.onLoadRatingsFailed();
            }
        });
    }

    @Override
    public void submitRatings(final RemixMovieRating remixMovieRating, final String documentId) {

        FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentReference movieReference = FirebaseFirestore
                        .getInstance()
                        .collection(RemixConstants.COLLECTION_MOVIES)
                        .document(documentId);
                DocumentReference ratingReference = movieReference.collection(RemixConstants.COLLECTION_RATINGS)
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid());

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
                movieDetailView.onSubmitRatings();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                movieDetailView.onSubmitRatingsFailed();
            }
        });
    }
}
