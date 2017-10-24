package com.vipshah.remixermovies.di;

import com.vipshah.remixermovies.ui.login.LoginActivity;
import com.vipshah.remixermovies.ui.login.LoginActivityModule;
import com.vipshah.remixermovies.ui.movies.detail.MovieDetailActivity;
import com.vipshah.remixermovies.ui.movies.detail.MovieDetailModule;
import com.vipshah.remixermovies.ui.movies.list.MovieListModule;
import com.vipshah.remixermovies.ui.movies.list.MoviesListActivity;
import com.vipshah.remixermovies.ui.review.ReviewModule;
import com.vipshah.remixermovies.ui.review.ReviewsActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = LoginActivityModule.class)
    abstract LoginActivity loginActivity();

    @ContributesAndroidInjector(modules = MovieListModule.class)
    abstract MoviesListActivity moviesListActivity();

    @ContributesAndroidInjector(modules = MovieDetailModule.class)
    abstract MovieDetailActivity movieDetailActivity();

    @ContributesAndroidInjector(modules = ReviewModule.class)
    abstract ReviewsActivity reviewsActivity();
}
