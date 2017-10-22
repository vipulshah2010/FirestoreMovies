package com.vipshah.remixermovies.ui.ratings;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.google.firebase.auth.FirebaseAuth;
import com.vipshah.remixermovies.R;
import com.vipshah.remixermovies.models.RemixMovieRating;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RatingsDialogFragment extends DialogFragment {

    private static final String ARG_RATINGS = "ratings";

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    private RatingListener mReviewListener;

    public static RatingsDialogFragment newInstance(float ratings) {

        Bundle args = new Bundle();
        args.putFloat(ARG_RATINGS, ratings);
        RatingsDialogFragment fragment = new RatingsDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_rating, container, false);
        ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ratingBar.setRating(getCurrentRatings());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof RatingListener) {
            mReviewListener = (RatingListener) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    @OnClick(R.id.submitButton)
    public void onSubmitRating() {

        RemixMovieRating rating = new RemixMovieRating();
        rating.setRatings(ratingBar.getRating());
        rating.setDate(new Date());
        rating.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        if (mReviewListener != null) {
            mReviewListener.submitRatings(rating);
        }

        dismiss();
    }

    @OnClick(R.id.cancelButton)
    public void dismissDialog() {
        dismiss();
    }

    private float getCurrentRatings() {
        return getArguments().getFloat(ARG_RATINGS);
    }

    public interface RatingListener {
        void submitRatings(RemixMovieRating remixMovieRating);
    }
}