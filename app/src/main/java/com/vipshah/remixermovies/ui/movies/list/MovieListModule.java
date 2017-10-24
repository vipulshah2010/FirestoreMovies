package com.vipshah.remixermovies.ui.movies.list;

import dagger.Binds;
import dagger.Module;

import static com.vipshah.remixermovies.ui.movies.list.MoviesListContract.MoviesListPresenter;
import static com.vipshah.remixermovies.ui.movies.list.MoviesListContract.MoviesListView;

@Module
public abstract class MovieListModule {

    @Binds
    abstract MoviesListPresenter<MoviesListView> providePresenter(MoviesListPresenterImpl<MoviesListView> presenter);
}
