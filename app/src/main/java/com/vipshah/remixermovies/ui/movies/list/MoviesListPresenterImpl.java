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
import com.vipshah.remixermovies.ui.CommonPresenter;
import com.vipshah.remixermovies.utils.CommonUtils;

import javax.inject.Inject;

public class MoviesListPresenterImpl<V extends MoviesListContract.MoviesListView> extends CommonPresenter<V>
        implements MoviesListContract.MoviesListPresenter<V> {

    private Context mContext;
    private FirebaseFirestore mFirebaseFirestore;

    @Inject
    MoviesListPresenterImpl(Context context, FirebaseFirestore firebaseFirestore) {
        mContext = context;
        mFirebaseFirestore = firebaseFirestore;
    }

    @Override
    public void fetchMovies() {
        Query query = mFirebaseFirestore.collection(RemixConstants.COLLECTION_MOVIES);
        getView().onFetchMovies(query);
    }

    @Override
    public void uploadMovies() {
        RemixMovie[] remixMovies = CommonUtils.getMockMovies(mContext).results;

        WriteBatch batch = mFirebaseFirestore.batch();

        for (RemixMovie movie : remixMovies) {
            DocumentReference movieReference = mFirebaseFirestore
                    .collection(RemixConstants.COLLECTION_MOVIES)
                    .document(movie.getTitle());
            batch.set(movieReference, movie);
        }

        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                getView().onUploadMovies(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                getView().onUploadMovies(false);
            }
        });
    }
}
