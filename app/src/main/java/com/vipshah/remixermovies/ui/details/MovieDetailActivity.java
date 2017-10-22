package com.vipshah.remixermovies.ui.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.squareup.picasso.Picasso;
import com.vipshah.remixermovies.R;
import com.vipshah.remixermovies.models.RemixMovie;
import com.vipshah.remixermovies.models.RemixMovieRating;
import com.vipshah.remixermovies.ui.ratings.RatingsDialogFragment;
import com.vipshah.remixermovies.ui.review.ReviewsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieDetailActivity extends AppCompatActivity implements RatingsDialogFragment.RatingListener {

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayMovieDetails();
    }

    private void displayMovieDetails() {
        String id = getDocumentId();
        DocumentReference reference = mFirebaseFirestore.collection("movies").document(id);

        mMovieListenerRegistration = reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                displayMovieDetails(documentSnapshot);
            }
        });
    }

    private void displayMovieDetails(DocumentSnapshot documentSnapshot) {
        if (documentSnapshot.exists()) {
            RemixMovie movie = documentSnapshot.toObject(RemixMovie.class);
            Picasso.with(this).load("https://image.tmdb.org/t/p/w780" + movie.getPoster())
                    .fit().into(headerImageView);
            collapsingToolbar.setTitle(movie.getTitle());
            ratedTextView.setText(String.format("Rated: %s", movie.isAdult() ? "18+" : "UA"));
            ratingBar.setRating(Math.round(movie.getAverageRatings()));
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
        Query query = mFirebaseFirestore.collection("movies")
                .document(getDocumentId()).collection("ratings").whereEqualTo("email", FirebaseAuth
                        .getInstance().getCurrentUser().getEmail());
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                if (documentSnapshots.isEmpty()) {
                    RatingsDialogFragment.newInstance(0).show(getSupportFragmentManager(), null);
                } else {
                    RemixMovieRating movieRating = documentSnapshots.getDocuments().get(0).toObject(RemixMovieRating.class);
                    RatingsDialogFragment.newInstance(movieRating.getRatings()).show(getSupportFragmentManager(), null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MovieDetailActivity.this, "Failed to fetch ratings!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getDocumentId() {
        return getIntent().getStringExtra(ARG_DOCUMENT_ID);
    }

    @Override
    public void onSubmitRating(final RemixMovieRating remixMovieRating) {
        mFirebaseFirestore.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentReference movieReference = mFirebaseFirestore.collection("movies").document(getDocumentId());
                DocumentReference ratingReference = movieReference.collection("ratings").document(FirebaseAuth
                        .getInstance().getCurrentUser().getUid());

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
        }).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Snackbar.make(findViewById(android.R.id.content), "Thanks for your rating",
                        Snackbar.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(findViewById(android.R.id.content), "Failed to add rating",
                        Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
