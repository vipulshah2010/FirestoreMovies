package com.vipshah.remixermovies.ui.review;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.vipshah.remixermovies.R;
import com.vipshah.remixermovies.RemixConstants;
import com.vipshah.remixermovies.models.RemixMovieReview;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReviewsActivity extends AppCompatActivity implements
        ReviewDialogFragment.ReviewListener, ReviewContract.ReviewView {

    private static final String ARG_DOCUMENT_ID = "id";

    @BindView(R.id.reviewsRecyclerView)
    RecyclerView reviewsRecyclerView;

    @BindView(R.id.noReviewContainer)
    View noReviewContainer;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ReviewsAdapter reviewsAdapter;

    private FirebaseFirestore firebaseFirestore;

    private ReviewContract.ReviewPresenter reviewPresenter;

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

        reviewPresenter = new ReviewPresenterPresenterImpl(this);
    }

    @Override
    protected void onDestroy() {
        if (reviewsAdapter != null) {
            reviewsAdapter.stopListeningForLiveEvents();
        }
        super.onDestroy();
    }

    @OnClick(R.id.addReviewButton)
    void addReview() {
        new ReviewDialogFragment().show(getSupportFragmentManager(), null);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!TextUtils.isEmpty(getDocumentId())) {
            Query query = firebaseFirestore.collection(RemixConstants.COLLECTION_MOVIES).document(getDocumentId())
                    .collection(RemixConstants.COLLECTION_REVIEWS).orderBy("date", Query.Direction.DESCENDING);

            reviewsAdapter = new ReviewsAdapter(query) {
                @Override
                public void onEventTriggered() {
                    reviewsRecyclerView.setVisibility(getItemCount() > 0 ? View.VISIBLE : View.GONE);
                    noReviewContainer.setVisibility(getItemCount() > 0 ? View.GONE : View.VISIBLE);
                }
            };

            reviewsAdapter.setListener(new ReviewsAdapter.ReviewAdapterListener() {
                @Override
                public void deleteReview(String reviewId) {
                    reviewPresenter.deleteReview(getDocumentId(), reviewId);
                }
            });

            reviewsRecyclerView.setAdapter(reviewsAdapter);

            reviewsAdapter.startListeningForLiveEvents();
        }
    }

    @Override
    public void submitReview(final RemixMovieReview review) {
        reviewPresenter.submitReview(getDocumentId(), review);
    }

    @Override
    public void onSubmitReview(boolean success) {
        if (success) {
            reviewsRecyclerView.smoothScrollToPosition(0);
        } else {
            Snackbar.make(findViewById(android.R.id.content), "Failed to set review", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDeleteReview(boolean success) {
        if (success) {
            Toast.makeText(ReviewsActivity.this, "Review Deleted!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ReviewsActivity.this, "Review Deletion Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    private String getDocumentId() {
        return getIntent().getStringExtra(ARG_DOCUMENT_ID);
    }

}
