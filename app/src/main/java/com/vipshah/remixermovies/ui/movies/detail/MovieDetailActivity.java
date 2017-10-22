package com.vipshah.remixermovies.ui.movies.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.squareup.picasso.Picasso;
import com.vipshah.remixermovies.R;
import com.vipshah.remixermovies.RemixConstants;
import com.vipshah.remixermovies.models.RemixMovie;
import com.vipshah.remixermovies.models.RemixMovieRating;
import com.vipshah.remixermovies.ui.ratings.RatingsDialogFragment;
import com.vipshah.remixermovies.ui.review.ReviewsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieDetailActivity extends AppCompatActivity implements RatingsDialogFragment.RatingListener, MovieDetailContract.MovieDetailView {

    private static final String ARG_DOCUMENT_ID = "id";

    @BindView(R.id.headerImageView)
    ImageView headerImageView;

    @BindView(R.id.ratedTextView)
    TextView ratedTextView;

    @BindView(R.id.totalVotesText)
    TextView totalVotesText;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.languageTextView)
    TextView languageTextView;

    @BindView(R.id.reviewsTextView)
    TextView reviewsTextView;

    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.overviewText)
    TextView overviewText;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private FirebaseFirestore mFirebaseFirestore;

    private ListenerRegistration mMovieListenerRegistration;

    private MovieDetailContract.MovieDetailPresenter movieDetailPresenter;

    public static Intent getIntent(Context context, String id) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(ARG_DOCUMENT_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);
        // RemixerBinder.bind(this);

        setSupportActionBar(toolbar);

        mFirebaseFirestore = FirebaseFirestore.getInstance();

        movieDetailPresenter = new MovieDetailPresenterImpl(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadMovieDetails();
    }

    private void loadMovieDetails() {
        DocumentReference reference = mFirebaseFirestore.collection(RemixConstants.COLLECTION_MOVIES)
                .document(getDocumentId());

        mMovieListenerRegistration = reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                loadMovieDetails(documentSnapshot);
            }
        });
    }

    private void loadMovieDetails(DocumentSnapshot documentSnapshot) {
        if (documentSnapshot.exists()) {
            RemixMovie movie = documentSnapshot.toObject(RemixMovie.class);
            Picasso.with(this).load(movie.getPoster()).fit().into(headerImageView);
            collapsingToolbar.setTitle(movie.getTitle());

            ratedTextView.setText(getString(R.string.movie_rated, movie.isAdult() ?
                    getString(R.string.rating_adult) : getString(R.string.rating_universal)));
            ratingBar.setRating(movie.getAverageRatings());
            languageTextView.setText(movie.getLanguage());
            overviewText.setText(movie.getOverview());
            reviewsTextView.setText(getString(R.string.review_count, String.valueOf(movie.getTotalReviews())));
            totalVotesText.setText(getString(R.string.total_votes, String.valueOf(movie.getTotalRatings())));
        }
    }

    @OnClick(R.id.reviewsTextView)
    void displayReviews() {
        startActivity(ReviewsActivity.getIntent(this, getDocumentId()));
    }

    @Override
    protected void onStop() {
        if (mMovieListenerRegistration != null) {
            mMovieListenerRegistration.remove();
            mMovieListenerRegistration = null;
        }
        super.onStop();
    }

    @OnClick(R.id.rateMovieButton)
    void displayRatingDialog() {
        movieDetailPresenter.loadRatings(getDocumentId());
    }

    @Override
    public void submitRatings(final RemixMovieRating remixMovieRating) {
        movieDetailPresenter.submitRatings(remixMovieRating, getDocumentId());
    }

    @Override
    public void onLoadRatingsSuccess(float ratings) {
        RatingsDialogFragment.newInstance(ratings).show(getSupportFragmentManager(), null);
    }

    @Override
    public void onLoadRatingsFailed() {
        Toast.makeText(MovieDetailActivity.this, "Failed to fetch ratings!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSubmitRatings() {
        Snackbar.make(findViewById(android.R.id.content), R.string.thanks_for_ratings,
                Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onSubmitRatingsFailed() {
        Snackbar.make(findViewById(android.R.id.content), R.string.failed_to_add_ratings,
                Snackbar.LENGTH_SHORT).show();
    }

    private String getDocumentId() {
        return getIntent().getStringExtra(ARG_DOCUMENT_ID);
    }

}
