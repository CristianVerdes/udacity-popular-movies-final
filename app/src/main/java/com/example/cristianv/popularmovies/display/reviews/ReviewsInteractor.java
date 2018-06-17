package com.example.cristianv.popularmovies.display.reviews;

import android.os.AsyncTask;

import com.example.cristianv.popularmovies.display.movie.MovieReviewModel;
import com.example.cristianv.popularmovies.display.movies.MovieViewHolder;
import com.example.cristianv.popularmovies.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * Created by cristian on 11/21/17.
 */

public class ReviewsInteractor implements ReviewsContract.Interactor {
    private int movieId;

    public ReviewsInteractor(int movieId) {
        this.movieId = movieId;
    }

    @Override
    public void getReviewsData(ReviewsContract.OnInteractorFinishedListener listener) {
        new ReviewsDataTask(listener).execute(
                NetworkUtils.createReviewsURL(String.valueOf(movieId))
        );
    }

    static class ReviewsDataTask extends AsyncTask<URL, Void, List<MovieReviewModel>>{
        private WeakReference<ReviewsContract.OnInteractorFinishedListener> listener;

        public ReviewsDataTask(ReviewsContract.OnInteractorFinishedListener listener){
            this.listener = new WeakReference<>(listener);
        }

        @Override
        protected List<MovieReviewModel> doInBackground(URL... urls) {
            List<MovieReviewModel> listItems = new ArrayList<>();
            URL url = urls[0];

            String queryResult = null;

            try {
                queryResult = NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject(queryResult);
                JSONArray jsonReviews = jsonObject.getJSONArray("results");
                for (int i = 0; i <= jsonReviews.length(); i++){
                    JSONObject jsonReview = jsonReviews.getJSONObject(i);
                    String author = jsonReview.getString("author");
                    String content = jsonReview.getString("content");
                    MovieReviewModel movieReviewModel = new MovieReviewModel(author, content);
                    listItems.add(movieReviewModel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return listItems;
        }

        @Override
        protected void onPostExecute(List<MovieReviewModel> movieReviewModels) {
            super.onPostExecute(movieReviewModels);
            if (listener.get() != null) {
                listener.get().onReviewsDataFinished(movieReviewModels);
            }
        }
    }
}
