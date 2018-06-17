package com.example.cristianv.popularmovies.display.movies;

import com.example.cristianv.popularmovies.display.movie.MovieContract;

import java.util.List;

/**
 * Created by cristianv on 9/5/17.
 */

public class MoviesPresenter implements MoviesContract.Presenter,
                                        MoviesContract.OnInteractorFinishedListener {
    private MoviesContract.View moviesView;
    private MoviesContract.Interactor interactor;

    public MoviesPresenter(MoviesContract.View moviesView,
                           MoviesContract.Interactor interactor){
        this.moviesView = moviesView;
        this.interactor = interactor;
    }

    // Presenter
    @Override
    public void onResume(Filter filter) {
        if (moviesView != null){
            moviesView.showProgress();
        }
        switch (filter){
            case POPULAR:
                interactor.getPopularData(this);
                break;
            case TOP_RATED:
                interactor.getTopRatedData(this);
                break;
            case FAVORITES:
                break;
            default:
                interactor.getPopularData(this);
        }
    }

    @Override
    public void onPopularSelected(){
        if (moviesView != null){
            moviesView.hideNoConnection();
            moviesView.showProgress();
        }

        interactor.getPopularData(this);
    }

    @Override
    public void onTopRatedSelected() {
        if (moviesView != null){
            moviesView.hideNoConnection();
            moviesView.showProgress();
        }

        interactor.getTopRatedData(this);
    }

    @Override
    public void onFavoritesSelected() {
        if (moviesView != null){
            moviesView.hideNoConnection();
            moviesView.showProgress();
        }
    }

    @Override
    public void onDestroy() {
        moviesView = null;
    }

    // On Interactor Finished Listener
    @Override
    public void onFinished(RequestResponse requestResponse) {
        if (moviesView != null && requestResponse.getException() == null){
            moviesView.hideProgress();
            moviesView.hideNoConnection();
            moviesView.setItems(requestResponse.getPosters());
        } else {
            moviesView.hideProgress();
            //moviesView.showMessage("No Connection");
            moviesView.showNoConnection();
        }
    }

    public MoviesContract.View getMoviesView() {
        return moviesView;
    }

    enum Filter{
        POPULAR,
        TOP_RATED,
        FAVORITES
    }
}
