package com.vipshah.remixermovies.ui.review;

import dagger.Binds;
import dagger.Module;

import static com.vipshah.remixermovies.ui.review.ReviewContract.ReviewPresenter;
import static com.vipshah.remixermovies.ui.review.ReviewContract.ReviewView;

@Module
public abstract class ReviewModule {

    @Binds
    abstract ReviewPresenter<ReviewView> providePresenter(ReviewPresenterImpl<ReviewView> presenter);
}
