package com.vipshah.remixermovies.ui.review;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.vipshah.remixermovies.R;
import com.vipshah.remixermovies.models.RemixMovieReview;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReviewDialogFragment extends DialogFragment {

    @BindView(R.id.reviewEditText)
    EditText mReviewEditText;

    private ReviewListener mReviewListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_review, container, false);
        ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ReviewListener) {
            mReviewListener = (ReviewListener) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    @OnClick(R.id.submitReviewButton)
    public void onSubmitReview() {

        String reviewText = mReviewEditText.getText().toString();

        if (TextUtils.isEmpty(reviewText)) {
            return;
        }

        RemixMovieReview review = new RemixMovieReview();
        review.setReview(reviewText);
        review.setDate(new Date());
        review.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        if (mReviewListener != null) {
            mReviewListener.onSubmitReview(review);
        }

        dismiss();
    }

    @OnClick(R.id.cancelButton)
    public void dismissDialog() {
        dismiss();
    }

    public interface ReviewListener {
        void onSubmitReview(RemixMovieReview review);
    }
}