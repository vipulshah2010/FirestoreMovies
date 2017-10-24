package com.vipshah.remixermovies.ui.review;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.vipshah.remixermovies.FirestoreAdapter;
import com.vipshah.remixermovies.R;
import com.vipshah.remixermovies.models.RemixMovieReview;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsAdapter extends FirestoreAdapter<ReviewsAdapter.ReviewViewHolder> {

    FirebaseUser mFirebaseUser;

    private ReviewAdapterListener listener;

    @Inject
    ReviewsAdapter(Query query, FirebaseUser firebaseUser) {
        super(query);
        mFirebaseUser = firebaseUser;
    }

    void setListener(ReviewAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public ReviewsAdapter.ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReviewViewHolder holder, int position) {
        final DocumentSnapshot documentSnapshot = mDocumentSnapshots.get(position);
        final RemixMovieReview review = documentSnapshot.toObject(RemixMovieReview.class);

        holder.reviewTextView.setText(review.getReview());
        holder.emailTextView.setText(review.getEmail());

        if (mFirebaseUser.getEmail().equalsIgnoreCase(review.getEmail())) {
            holder.deleteReviewButton.setVisibility(View.VISIBLE);
        } else {
            holder.deleteReviewButton.setVisibility(View.GONE);
        }

        holder.deleteReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.deleteReview(mDocumentSnapshots.get(holder.getAdapterPosition()).getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDocumentSnapshots.size();
    }

    interface ReviewAdapterListener {
        void deleteReview(String id);
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.reviewTextView)
        TextView reviewTextView;

        @BindView(R.id.emailTextView)
        TextView emailTextView;

        @BindView(R.id.deleteReviewButton)
        View deleteReviewButton;

        ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
