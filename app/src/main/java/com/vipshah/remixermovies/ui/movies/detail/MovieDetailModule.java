package com.vipshah.remixermovies.ui.movies.detail;

import dagger.Binds;
import dagger.Module;

import static com.vipshah.remixermovies.ui.movies.detail.MovieDetailContract.MovieDetailPresenter;
import static com.vipshah.remixermovies.ui.movies.detail.MovieDetailContract.MovieDetailView;

@Module
public abstract class MovieDetailModule {

    @Binds
    abstract MovieDetailPresenter<MovieDetailView> providePresenter(MovieDetailPresenterImpl<MovieDetailView> presenter);
}
