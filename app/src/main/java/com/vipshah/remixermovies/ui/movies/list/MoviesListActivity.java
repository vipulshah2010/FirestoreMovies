package com.vipshah.remixermovies.ui.movies.list;

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
import com.google.firebase.firestore.Query;
import com.vipshah.remixermovies.R;
import com.vipshah.remixermovies.ui.movies.detail.MovieDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesListActivity extends AppCompatActivity implements MoviesListContract.MoviesListView {

    @BindView(R.id.moviesRecyclerView)
    RecyclerView mMoviesRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private MoviesAdapter mMoviesAdapter;

    private LinearLayoutManager mLinearLayoutManager;
    private GridLayoutManager mGridLayoutManager;
    private MoviesListContract.MoviesListPresenter moviesListPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        ButterKnife.bind(this);
        RemixerBinder.bind(this);
        setSupportActionBar(toolbar);

        RemixerFragment.newInstance().attachToFab(this, (FloatingActionButton) findViewById(R.id.remixerFab));

        moviesListPresenter = new MoviesListPresenterImpl(this);

        mGridLayoutManager = new GridLayoutManager(this, 2);
        mLinearLayoutManager = new LinearLayoutManager(this);

        mMoviesRecyclerView.setLayoutManager(mGridLayoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        moviesListPresenter.fetchMovies();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_upload_movies:
                moviesListPresenter.uploadMovies(this);
                return true;
            case R.id.action_delete_movies:
                deleteAllMovies();
                return true;
        }
        return false;
    }

    private void deleteAllMovies() {

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem uploadMoviesItem = menu.findItem(R.id.action_upload_movies);
        MenuItem deleteMoviesItem = menu.findItem(R.id.action_delete_movies);
        uploadMoviesItem.setVisible(mMoviesAdapter.getItemCount() == 0);
        deleteMoviesItem.setVisible(mMoviesAdapter.getItemCount() > 0);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @BooleanVariableMethod(initialValue = true, title = "Show Movie Details")
    void showMovieDetails(Boolean show) {
        if (mMoviesAdapter != null) {
            mMoviesAdapter.showDetailSection(show);
        }
    }

    @BooleanVariableMethod(title = "Show Numeric Ratings")
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

    @Override
    protected void onDestroy() {
        mMoviesAdapter.stopListeningForLiveEvents();
        super.onDestroy();
    }

    @Override
    public void onFetchMovies(Query query) {
        mMoviesAdapter = new MoviesAdapter(query) {
            @Override
            public void onEventTriggered() {
                mMoviesRecyclerView.setVisibility(getItemCount() > 0 ? View.VISIBLE : View.GONE);
                invalidateOptionsMenu();
            }
        };

        mMoviesAdapter.startListeningForLiveEvents();

        mMoviesAdapter.setListener(new MoviesAdapter.MoviesAdapterListener() {
            @Override
            public void onMovieSelected(DocumentSnapshot documentSnapshot) {
                startActivity(MovieDetailActivity.getIntent(MoviesListActivity.this, documentSnapshot.getId()));
            }
        });

        mMoviesRecyclerView.setAdapter(mMoviesAdapter);
    }

    @Override
    public void onUploadMovies(boolean success) {
        // no need to take any action as we have set live listener adapter for changes.
        // onEventTriggered will be fired automatically
    }
}
