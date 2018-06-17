package com.example.cristianv.popularmovies.display.movies;

import com.example.cristianv.popularmovies.display.movie.MovieModel;

import java.util.List;

/**
 * Created by cristianv on 9/5/17.
 */

public interface MoviesContract {

    interface View {

        void showProgress();

        void hideProgress();

        void setItems(List<MoviesModel> items);

        void showNoConnection();

        void hideNoConnection();
    }

    interface Presenter {
        void onResume(MoviesPresenter.Filter filter);

        void onDestroy();

        void onPopularSelected();

        void onTopRatedSelected();

        void onFavoritesSelected();
    }

    interface OnInteractorFinishedListener {
        void onFinished(RequestResponse requestResponse);
    }

    interface OnInteractorFavoritesFinishedListener {
        void onFavFinished(List<MovieModel> movies);
    }

    interface Interactor {
        void getPopularData(OnInteractorFinishedListener listener);

        void getTopRatedData(OnInteractorFinishedListener listener);

    }

}
