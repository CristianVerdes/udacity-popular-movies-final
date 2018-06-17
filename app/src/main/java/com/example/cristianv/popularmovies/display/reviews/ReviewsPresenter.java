package com.example.cristianv.popularmovies.display.reviews;

import com.example.cristianv.popularmovies.display.movie.MovieReviewModel;

import java.util.List;

/**
 * Created by cristian on 11/21/17.
 */

public class ReviewsPresenter implements ReviewsContract.Presenter,
                                            ReviewsContract.OnInteractorFinishedListener{
    private ReviewsContract.View reviewsView;
    private ReviewsContract.Interactor interactor;

    public ReviewsPresenter (ReviewsContract.View reviewsView,
                             ReviewsContract.Interactor interactor){
        this.reviewsView = reviewsView;
        this.interactor = interactor;
    }

    @Override
    public void onResume() {
        reviewsView.showProgress();
        interactor.getReviewsData(this);
    }

    @Override
    public void onDestroy() {
        reviewsView = null;
    }

    // On Interactor Finished Listener
    @Override
    public void onReviewsDataFinished(List<MovieReviewModel> reviews) {
        if(reviewsView != null && reviews != null){
            reviewsView.hideProgress();
            reviewsView.setItems(reviews);
        } else {
            reviewsView.hideProgress();
            reviewsView.showNoConnection();
            // Or show "No data" error
        }

    }
}
