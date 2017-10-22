package com.vipshah.remixermovies.ui.movies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.libraries.remixer.annotation.BooleanVariableMethod;
import com.google.android.libraries.remixer.annotation.RemixerBinder;
import com.google.android.libraries.remixer.ui.view.RemixerFragment;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.vipshah.remixermovies.R;
import com.vipshah.remixermovies.models.RemixMovie;
import com.vipshah.remixermovies.ui.details.MovieDetailActivity;
import com.vipshah.remixermovies.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieListActivity extends AppCompatActivity {

    @BindView(R.id.moviesRecyclerView)
    RecyclerView mMoviesRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private FirebaseFirestore mFirebaseFirestore;
    private MoviesAdapter mMoviesAdapter;

    private LinearLayoutManager mLinearLayoutManager;
    private GridLayoutManager mGridLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        ButterKnife.bind(this);
        RemixerBinder.bind(this);

        setSupportActionBar(toolbar);

        mGridLayoutManager = new GridLayoutManager(this, 2);
        mLinearLayoutManager = new LinearLayoutManager(this);

        RemixerFragment.newInstance().attachToFab(this, (FloatingActionButton) findViewById(R.id.remixerFab));

        mFirebaseFirestore = FirebaseFirestore.getInstance();

        mMoviesRecyclerView.setLayoutManager(mGridLayoutManager);

        fetchMovies();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_upload:
                uploadMovies();
                return true;
            case R.id.action_delete:
                deleteAllMovies();
                return true;
        }
        return false;
    }

    private void deleteAllMovies() {

    }

    private void uploadMovies() {
        RemixMovie[] remixMovies = CommonUtils.getMockMovies(this).results;
        for (RemixMovie movie : remixMovies) {
            mFirebaseFirestore.collection("movies").document(movie.getTitle()).set(movie);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movies_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void fetchMovies() {
        Query query = mFirebaseFirestore.collection("movies");
        mMoviesAdapter = new MoviesAdapter(query) {
            @Override
            public void onEventTriggered() {
                mMoviesRecyclerView.setVisibility(getItemCount() > 0 ? View.VISIBLE : View.GONE);
            }
        };
        mMoviesAdapter.startListeningForLiveEvents();

        mMoviesAdapter.setListener(new MoviesAdapter.MoviesAdapterListener() {
            @Override
            public void onMovieSelected(DocumentSnapshot documentSnapshot) {
                startActivity(MovieDetailActivity.getIntent(MovieListActivity.this, documentSnapshot.getId()));
            }
        });

        mMoviesRecyclerView.setAdapter(mMoviesAdapter);
    }

    @BooleanVariableMethod(initialValue = true, title = "Show Movie Details")
    void showMovieDetails(Boolean show) {
        if (mMoviesAdapter != null) {
            mMoviesAdapter.showDetailSection(show);
        }
    }

    @BooleanVariableMethod(initialValue = true, title = "Show Numeric Ratings")
    void showNumericRatings(Boolean show) {
        if (mMoviesAdapter != null) {
            mMoviesAdapter.showNumericRatings(show);
        }
    }

    @BooleanVariableMethod(initialValue = true, title = "Grid Style")
    void setGridLayoutManager(Boolean gridStyle) {
        if (mMoviesRecyclerView != null && mMoviesAdapter != null) {
            mMoviesRecyclerView.setLayoutManager(gridStyle ? mGridLayoutManager : mLinearLayoutManager);
            mMoviesAdapter.notifyDataSetChanged();
        }
    }
}
