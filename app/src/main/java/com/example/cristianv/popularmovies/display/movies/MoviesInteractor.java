package com.example.cristianv.popularmovies.display.movies;

import android.content.AsyncTaskLoader;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.cristianv.popularmovies.display.movie.MovieModel;
import com.example.cristianv.popularmovies.display.movie.MovieReviewModel;
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
 * Created by cristianv on 9/5/17.
 */

public class MoviesInteractor implements MoviesContract.Interactor {

    @Override
    public void getPopularData(final MoviesContract.OnInteractorFinishedListener listener) {
        new MyPopularMovies(listener).execute(NetworkUtils.createPopularURL());
    }

    @Override
    public void getTopRatedData(MoviesContract.OnInteractorFinishedListener listener) {
        new MyTopRatedMovies(listener).execute(NetworkUtils.createTopRatedURL());

    }

    static class MyPopularMovies extends AsyncTask<URL, Void, RequestResponse>{
        private WeakReference<MoviesContract.OnInteractorFinishedListener> listener;

        public MyPopularMovies(MoviesContract.OnInteractorFinishedListener listener){
            this.listener = new WeakReference<>(listener);
        }

        @Override
        protected RequestResponse doInBackground(URL... urls) {
            RequestResponse requestResponse = new RequestResponse();

            List<MoviesModel> posters = new ArrayList<>();

            URL url = urls[0];
            String queryResult = null;

            try {
                queryResult = NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
                requestResponse.setException(e);
                return requestResponse;
            }

            try {
                JSONObject jsonObject = new JSONObject(queryResult);

                JSONArray jsonResults = jsonObject.getJSONArray("results");

                for (int i = 0; i < jsonResults.length(); i++){
                    JSONObject movie = jsonResults.getJSONObject(i);
                    String posterPath = movie.getString("poster_path");
                    int movieId = movie.getInt("id");

                    MoviesModel moviesModel = new MoviesModel(NetworkUtils.createPosterLink(posterPath), movieId);

                    posters.add(moviesModel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            requestResponse.setPosters(posters);

            return requestResponse;
        }

        @Override
        protected void onPostExecute(RequestResponse requestResponse) {
            if (listener.get() != null) {
                listener.get().onFinished(requestResponse);
            }
        }
    }

    static class MyTopRatedMovies extends AsyncTask<URL, Void, RequestResponse>{
        private WeakReference<MoviesContract.OnInteractorFinishedListener> listener;

        public MyTopRatedMovies(MoviesContract.OnInteractorFinishedListener listener){
            this.listener = new WeakReference<>(listener);
        }

        @Override
        protected RequestResponse doInBackground(URL... urls) {
            RequestResponse requestResponse = new RequestResponse();
            List<MoviesModel> posters = new ArrayList<>();

            URL url = urls[0];
            String queryResult = null;

            try {
                queryResult = NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
                requestResponse.setException(e);
                return requestResponse;
            }

            try {
                JSONObject jsonObject = new JSONObject(queryResult);

                JSONArray jsonResults = jsonObject.getJSONArray("results");

                for (int i = 0; i < jsonResults.length(); i++){
                    JSONObject movie = jsonResults.getJSONObject(i);
                    String posterPath = movie.getString("poster_path");
                    int movieId = movie.getInt("id");

                    MoviesModel moviesModel = new MoviesModel(NetworkUtils.createPosterLink(posterPath), movieId);

                    posters.add(moviesModel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            requestResponse.setPosters(posters);

            return requestResponse;
        }

        @Override
        protected void onPostExecute(RequestResponse requestResponse) {
            if (listener.get() != null) {
                listener.get().onFinished(requestResponse);
            }
        }
    }
}
