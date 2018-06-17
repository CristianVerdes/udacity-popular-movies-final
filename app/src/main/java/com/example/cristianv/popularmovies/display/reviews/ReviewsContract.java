package com.example.cristianv.popularmovies.display.reviews;

import com.example.cristianv.popularmovies.display.movie.MovieReviewModel;

import java.util.List;

/**
 * Created by cristian on 11/21/17.
 */

public interface ReviewsContract {

    interface View{
        void showProgress();

        void hideProgress();

        void showNoConnection();

        void hideNoConnection();

        void setItems(List<MovieReviewModel> reviews);
    }

    interface Presenter{
        void onResume();

        void onDestroy();
    }

    interface OnInteractorFinishedListener{
        void onReviewsDataFinished(List<MovieReviewModel> reviews);
    }

    interface Interactor{
        void getReviewsData(OnInteractorFinishedListener listener);
    }

    interface ListItemClickListener{
        void onListItemClick(MovieReviewModel movieReviewModel);
    }

}
