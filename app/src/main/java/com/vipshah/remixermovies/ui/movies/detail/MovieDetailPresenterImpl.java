package com.vipshah.remixermovies.ui.movies.detail;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vipshah.remixermovies.RemixConstants;
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
        // TODO - Step-4 Load Ratings and call following

        getView().onLoadRatings(true, 0);
    }

    @Override
    public void submitRatings(final RemixMovieRating remixMovieRating, final String documentId) {
        // TODO - Step-5 submit ratings and call following
        getView().onSubmitRatings(false);
    }

    @Override
    public void deleteMovie(String documentId) {
        if (!TextUtils.isEmpty(documentId)) {
            mFirebaseFirestore.collection(RemixConstants.COLLECTION_MOVIES)
                    .document(documentId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    getView().onMovieDeleted(true);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    getView().onMovieDeleted(false);
                }
            });
        }
    }
}
