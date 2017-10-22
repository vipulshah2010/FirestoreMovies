package com.vipshah.remixermovies.ui.movies.list;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;
import com.vipshah.remixermovies.RemixConstants;
import com.vipshah.remixermovies.models.RemixMovie;
import com.vipshah.remixermovies.utils.CommonUtils;

public class MoviesListPresenterImpl implements MoviesListContract.MoviesListPresenter {

    private MoviesListContract.MoviesListView moviesListView;

    MoviesListPresenterImpl(MoviesListContract.MoviesListView moviesListView) {
        this.moviesListView = moviesListView;
    }

    @Override
    public void fetchMovies() {
        Query query = FirebaseFirestore.getInstance().collection(RemixConstants.COLLECTION_MOVIES);
        moviesListView.onFetchMovies(query);
    }

    @Override
    public void uploadMovies(Context context) {
        RemixMovie[] remixMovies = CommonUtils.getMockMovies(context).results;

        WriteBatch batch = FirebaseFirestore.getInstance().batch();

        for (RemixMovie movie : remixMovies) {
            DocumentReference movieReference = FirebaseFirestore
                    .getInstance()
                    .collection(RemixConstants.COLLECTION_MOVIES)
                    .document(movie.getTitle());
            batch.set(movieReference, movie);
        }

        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                moviesListView.onUploadMovies(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                moviesListView.onUploadMovies(false);
            }
        });
    }
}
