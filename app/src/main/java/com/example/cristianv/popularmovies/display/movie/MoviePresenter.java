package com.example.cristianv.popularmovies.display.movie;

import java.util.List;

/**
 * Created by cristianv on 9/14/17.
 */

public class MoviePresenter implements MovieContract.Presenter,
                                       MovieContract.OnInteractorFinishedListener{
    private MovieContract.View movieView;
    private MovieContract.Interactor interactor;

    public MoviePresenter(MovieContract.View movieView,
                          MovieContract.Interactor interactor){
        this.movieView = movieView;
        this.interactor = interactor;
    }

    @Override
    public void onResume() {
        if(movieView != null){
            movieView.showProgress();
        }
        interactor.getMovieData(this);
    }

    @Override
    public void onDestroy() {
        movieView = null;
    }

    // On Interactor finished listener
    @Override
    public void onMovieDataFinished(MovieModel movieModel) {
        if (movieView != null && movieModel != null){
            movieView.hideProgress();
            movieView.hideNoConnection();
            movieView.displayData(movieModel);
        } else {
            movieView.hideProgress();
            movieView.showNoConnection();
        }
    }

    @Override
    public void onReviewDataFinished(MovieReviewModel movieReviewModel) {
        if (movieView != null  && movieReviewModel != null){
            movieView.displayData(movieReviewModel);
        } else {
            movieView.showNoReviews();
        }

    }

    @Override
    public void onVideosDataFinished(List<String> videos) {
        if (videos != null){
            movieView.showVideos(videos);
        }
    }
}
