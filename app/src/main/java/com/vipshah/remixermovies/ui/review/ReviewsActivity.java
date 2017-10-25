package com.vipshah.remixermovies.ui.review;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.vipshah.remixermovies.R;
import com.vipshah.remixermovies.RemixConstants;
import com.vipshah.remixermovies.models.RemixMovieReview;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;

import static com.vipshah.remixermovies.ui.review.ReviewContract.ReviewPresenter;
import static com.vipshah.remixermovies.ui.review.ReviewContract.ReviewView;

public class ReviewsActivity extends DaggerAppCompatActivity implements
        InputReviewDialogFragment.ReviewListener, ReviewView {

    private static final String ARG_DOCUMENT_ID = "id";

    @BindView(R.id.reviewsRecyclerView)
    RecyclerView reviewsRecyclerView;

    @BindView(R.id.noReviewContainer)
    View noReviewContainer;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    ReviewPresenter<ReviewView> mPresenter;

    @Inject
    FirebaseFirestore mFirebaseFirestore;

    @Inject
    Lazy<FirebaseUser> mFirebaseUser;

    private ReviewsAdapter reviewsAdapter;

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

        mPresenter.attach(this);
    }

    @Override
    protected void onDestroy() {
        if (reviewsAdapter != null) {
            reviewsAdapter.stopListeningForLiveEvents();
        }
        mPresenter.detach();
        super.onDestroy();
    }

    @OnClick(R.id.addReviewButton)
    void addReview() {
        new InputReviewDialogFragment().show(getSupportFragmentManager(), null);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!TextUtils.isEmpty(getDocumentId())) {
            Query query = mFirebaseFirestore
                    .collection(RemixConstants.COLLECTION_MOVIES)
                    .document(getDocumentId())
                    .collection(RemixConstants.COLLECTION_REVIEWS)
                    .orderBy("date", Query.Direction.DESCENDING);

            reviewsAdapter = new ReviewsAdapter(query, mFirebaseUser.get()) {
                @Override
                public void onEventTriggered() {
                    reviewsRecyclerView.setVisibility(getItemCount() > 0 ? View.VISIBLE : View.GONE);
                    noReviewContainer.setVisibility(getItemCount() > 0 ? View.GONE : View.VISIBLE);
                }
            };

            reviewsAdapter.setListener(new ReviewsAdapter.ReviewAdapterListener() {
                @Override
                public void deleteReview(String reviewId) {
                    mPresenter.deleteReview(getDocumentId(), reviewId);
                }
            });

            reviewsRecyclerView.setAdapter(reviewsAdapter);

            reviewsAdapter.startListeningForLiveEvents();
        }
    }

    @Override
    public void submitReview(final RemixMovieReview review) {
        mPresenter.submitReview(getDocumentId(), review);
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

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
