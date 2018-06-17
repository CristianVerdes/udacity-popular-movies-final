package com.example.cristianv.popularmovies.display.movie;

import java.util.List;

/**
 * Created by cristianv on 9/14/17.
 */

public interface MovieContract {

     interface View{
        void showProgress();

        void hideProgress();

        void displayData(MovieModel movieModel);

        void displayData(MovieReviewModel movieReviewModel);

        void showNoReviews();

        void showNoConnection();

        void hideNoConnection();

        void showVideos(List<String> videos);

    }

    interface Presenter {
         void onResume();

         void onDestroy();
    }

    interface OnInteractorFinishedListener {
         void onMovieDataFinished(MovieModel movieModel);

         void onReviewDataFinished(MovieReviewModel movieReviewModel);

         void onVideosDataFinished(List<String> movieKey);
    }

    interface OnPlayVideoListener {
         void onPlayVideoClicked(String s);
    }

    interface Interactor{
         void getMovieData(OnInteractorFinishedListener listener);

         void getVideosData(OnInteractorFinishedListener listener);
    }

}
