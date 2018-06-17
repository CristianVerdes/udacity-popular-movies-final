package com.example.cristianv.popularmovies;

import android.app.Application;

import com.example.cristianv.popularmovies.display.movies.MoviesModel;

import java.util.List;

/**
 * Created by cristian.verdes on 21.02.2018.
 */

public class MyApplication extends Application {
    private static List<MoviesModel> movies;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static List<MoviesModel> getMovies() {
        return movies;
    }

    public static void setMovies(List<MoviesModel> movies) {
        MyApplication.movies = movies;
    }
}
