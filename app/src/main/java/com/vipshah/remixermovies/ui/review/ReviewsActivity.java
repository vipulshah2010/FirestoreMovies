package com.vipshah.remixermovies.ui.review;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Transaction;
import com.vipshah.remixermovies.R;
import com.vipshah.remixermovies.models.RemixMovie;
import com.vipshah.remixermovies.models.RemixMovieReview;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReviewsActivity extends AppCompatActivity implements ReviewDialogFragment.ReviewListener {

    private static final String ARG_DOCUMENT_ID = "id";

    @BindView(R.id.reviewsRecyclerView)
    RecyclerView reviewsRecyclerView;

    @BindView(R.id.noReviewContainer)
    View noReviewContainer;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ReviewsAdapter reviewsAdapter;

    private FirebaseFirestore firebaseFirestore;

    public static Intent getIntent(Context context, String id) {
        Intent intent = new Intent(context, ReviewsActivity.class);
        intent.putExtra(ARG_DOCUMENT_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void onStop() {
        if (reviewsAdapter != null) {
            reviewsAdapter.stopListeningForLiveEvents();
        }
        super.onStop();
    }

    @OnClick(R.id.addReviewButton)
    void addReview() {
        new ReviewDialogFragment().show(getSupportFragmentManager(), null);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!TextUtils.isEmpty(getDocumentId())) {
            Query query = firebaseFirestore.collection("movies").document(getDocumentId())
                    .collection("reviews").orderBy("date", Query.Direction.DESCENDING);

            reviewsAdapter = new ReviewsAdapter(query) {
                @Override
                public void onEventTriggered() {
                    reviewsRecyclerView.setVisibility(getItemCount() > 0 ? View.VISIBLE : View.GONE);
                    noReviewContainer.setVisibility(getItemCount() > 0 ? View.GONE : View.VISIBLE);
                }
            };

            reviewsAdapter.setListener(new ReviewsAdapter.ReviewAdapterListener() {
                @Override
                public void onReviewDeleted(String id) {
                    onDeleteReview(id);
                }
            });

            reviewsRecyclerView.setAdapter(reviewsAdapter);

            reviewsAdapter.startListeningForLiveEvents();
        }
    }

    private void onDeleteReview(final String id) {
        firebaseFirestore.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                final DocumentReference movieReference = firebaseFirestore.collection("movies")
                        .document(getDocumentId());
                final DocumentReference reviewReference = firebaseFirestore
                        .collection("movies")
                        .document(getDocumentId())
                        .collection("reviews")
                        .document(id);

                RemixMovie movie = transaction.get(movieReference).toObject(RemixMovie.class);
                movie.setTotalReviews(movie.getTotalReviews() - 1);

                transaction.delete(reviewReference);
                transaction.set(movieReference, movie);

                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ReviewsActivity.this, "Review Deleted!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ReviewsActivity.this, "Review Deletion Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getDocumentId() {
        return getIntent().getStringExtra(ARG_DOCUMENT_ID);
    }

    @Override
    public void onSubmitReview(final RemixMovieReview review) {
        final DocumentReference movieReference = firebaseFirestore.collection("movies").document(getDocumentId());
        final DocumentReference reviewReference = firebaseFirestore
                .collection("movies")
                .document(getDocumentId())
                .collection("reviews")
                .document();

        firebaseFirestore.runTransaction(new Transaction.Function<Void>() {
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
                reviewsRecyclerView.smoothScrollToPosition(0);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(findViewById(android.R.id.content), "Failed to set review",
                        Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
