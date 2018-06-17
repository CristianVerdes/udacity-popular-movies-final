package com.example.cristianv.popularmovies.display.movie;

import android.os.AsyncTask;

import com.example.cristianv.popularmovies.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cristianv on 9/14/17.
 */

public class MovieInteractor implements MovieContract.Interactor {
    private static String moviePoster;
    private static int movieId;

    public MovieInteractor(String moviePoster, int movieId){
            this.moviePoster = moviePoster;
            this.movieId = movieId;
    }

    // Get Movie Details
    @Override
    public void getMovieData(MovieContract.OnInteractorFinishedListener listener) {
        new MyMovieData(listener).execute(
                NetworkUtils.createMovieURL(String.valueOf(movieId)));
        new ReviewData(listener).execute(
                NetworkUtils.createReviewsURL(String.valueOf(movieId)));

        getVideosData(listener);
    }

    // Get Movie Data
    static class MyMovieData extends AsyncTask<URL, Void, MovieModel>{
        private WeakReference<MovieContract.OnInteractorFinishedListener> listener;

        public MyMovieData (MovieContract.OnInteractorFinishedListener listener){
            this.listener = new WeakReference<>(listener);
        }

        @Override
        protected MovieModel doInBackground(URL... urls) {
            URL url = urls[0];

            String movieDataResult;
            MovieModel movieModel = new MovieModel();

            try {
                movieDataResult = NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            try {
                JSONObject jsonObject = new JSONObject(movieDataResult);

                String title = jsonObject.getString("original_title");
                String overview = jsonObject.getString("overview");
                int voteAverage = jsonObject.getInt("vote_average");
                String releaseDate = jsonObject.getString("release_date");
                String background = jsonObject.getString("backdrop_path");

                movieModel.setPoster(moviePoster);
                movieModel.setTitle(title);
                movieModel.setOverview(overview);
                movieModel.setVoteAverage(String.valueOf(voteAverage));
                movieModel.setReleaseDate(releaseDate);
                movieModel.setBackground(background);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return movieModel;
        }

        @Override
        protected void onPostExecute(MovieModel movieModel) {
            super.onPostExecute(movieModel);
            if(listener.get() != null){
                listener.get().onMovieDataFinished(movieModel);
            }
        }
    }

    // Get Movie Review
    static class ReviewData extends AsyncTask<URL, Void, MovieReviewModel>{
        private WeakReference<MovieContract.OnInteractorFinishedListener> listener;

        public ReviewData (MovieContract.OnInteractorFinishedListener listener){
            this.listener = new WeakReference<>(listener);
        }

        @Override
        protected MovieReviewModel doInBackground(URL... urls) {
            URL url = urls[0];

            String queryResult = null;
            MovieReviewModel movieReviewModel = null;

            try {
                queryResult = NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject(queryResult);
                JSONArray jsonReviews = jsonObject.getJSONArray("results");
                JSONObject jsonFirstReview = jsonReviews.getJSONObject(0);
                String author = jsonFirstReview.getString("author");
                String content = jsonFirstReview.getString("content");

                movieReviewModel = new MovieReviewModel(author, content);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return movieReviewModel;
        }

        @Override
        protected void onPostExecute(MovieReviewModel movieReviewModel) {
            super.onPostExecute(movieReviewModel);
            if (listener.get() != null) {
                listener.get().onReviewDataFinished(movieReviewModel);
            }

        }
    }

    // Get Movie Trailer
    @Override
    public void getVideosData(MovieContract.OnInteractorFinishedListener listener){
        URL videoURL = NetworkUtils.getVideoURL(String.valueOf(movieId));
        new VideosData(listener).execute(videoURL);
    }

    static class VideosData extends AsyncTask<URL, Void, List<String>> {
        private WeakReference<MovieContract.OnInteractorFinishedListener> listener;

        public VideosData(MovieContract.OnInteractorFinishedListener listener){
            this.listener = new WeakReference<>(listener);
        }

        @Override
        protected List<String> doInBackground(URL... urls) {
            List<String> videos = new ArrayList<>();

            URL url = urls[0];

            String queryResult = null;
            String videoKey = null;

            try {
                queryResult = NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject(queryResult);
                JSONArray jsonTrailers = jsonObject.getJSONArray("results");

                for (int i = 0; i < jsonTrailers.length(); i++) {
                    JSONObject jsonObjectTrailer = jsonTrailers.getJSONObject(i);
                    String videoSource = jsonObjectTrailer.getString("site");
                    if (videoSource.equals("YouTube")) {
                        videos.add(jsonObjectTrailer.getString("key"));
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return videos;
        }

        @Override
        protected void onPostExecute(List<String> movies) {
            super.onPostExecute(movies);
            if (listener.get() != null){
                listener.get().onVideosDataFinished(movies);
            }

        }
    }

}
