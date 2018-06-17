package com.example.cristianv.popularmovies.display.movies;

/**
 * Created by cristianv on 9/5/17.
 */

public class MoviesModel {
    private String photoPath;
    private int movieId;

    public MoviesModel(String photoPath, int movieId){
        this.photoPath = photoPath;
        this.movieId = movieId;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
