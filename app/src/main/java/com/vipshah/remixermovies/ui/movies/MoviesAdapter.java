package com.vipshah.remixermovies.ui.movies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;
import com.vipshah.remixermovies.FirestoreAdapter;
import com.vipshah.remixermovies.R;
import com.vipshah.remixermovies.models.RemixMovie;
import com.vipshah.remixermovies.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends FirestoreAdapter<MoviesAdapter.MoviesViewHolder> {

    private MoviesAdapterListener mListener;

    private boolean mShowDetailSection = true;
    private boolean mShowRatingBar = true;

    MoviesAdapter(Query query) {
        super(query);
    }

    public void setListener(MoviesAdapterListener listener) {
        this.mListener = listener;
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_movie, parent, false);
        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MoviesViewHolder holder, int position) {
        final DocumentSnapshot documentSnapshot = mDocumentSnapshots.get(position);
        final RemixMovie movie = documentSnapshot.toObject(RemixMovie.class);

        Picasso.with(holder.itemView.getContext()).load("https://image.tmdb.org/t/p/w780" + movie.getPoster())
                .fit().into(holder.movieImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onMovieSelected(documentSnapshot);
                }
            }
        });

        holder.ratingBar.setRating(Math.round(movie.getAverageRatings()));

        holder.movieLabelText.setText(movie.getTitle());
        holder.movieDateText.setText(movie.getReleaseDate());
        holder.movieRatingsText.setText(String.format("Ratings %s", "" + movie.getAverageRatings()));

        holder.movieInfoContainer.setVisibility(mShowDetailSection ? View.VISIBLE : View.GONE);
        holder.movieInfoContainer.setBackgroundColor(CommonUtils.getRandomColor());


        if (!mShowDetailSection) {
            holder.ratingContainer.setVisibility(View.VISIBLE);
        }

        holder.ratingContainer.setVisibility(mShowRatingBar ? View.VISIBLE : View.GONE);
        holder.movieRatingsText.setVisibility(mShowRatingBar ? View.GONE : View.VISIBLE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onMovieSelected(mDocumentSnapshots.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    void showDetailSection(boolean show) {
        mShowDetailSection = show;
        notifyDataSetChanged();
    }

    void showNumericRatings(boolean show) {
        mShowRatingBar = !show;
        notifyDataSetChanged();
    }

    @Override

    public int getItemCount() {
        return mDocumentSnapshots.size();
    }

    public interface MoviesAdapterListener {
        void onMovieSelected(DocumentSnapshot firebaseMovie);
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.movieImageView)
        ImageView movieImageView;

        @BindView(R.id.ratingBar)
        RatingBar ratingBar;

        @BindView(R.id.movieInfoContainer)
        View movieInfoContainer;

        @BindView(R.id.movieLabelText)
        TextView movieLabelText;

        @BindView(R.id.movieDateText)
        TextView movieDateText;

        @BindView(R.id.movieRatingsText)
        TextView movieRatingsText;

        @BindView(R.id.ratingContainer)
        View ratingContainer;

        MoviesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
