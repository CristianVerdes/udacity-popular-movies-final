package com.example.cristianv.popularmovies.display.movies;

import java.io.IOException;
import java.util.List;

/**
 * Created by cristianv on 9/14/17.
 */

public class RequestResponse {
    private IOException exception;
    List<MoviesModel> posters;

    // Constructors
    public RequestResponse(){
    }

    public RequestResponse(IOException exception) {
        this.exception = exception;
    }

    public RequestResponse(List<MoviesModel> posters) {
        this.posters = posters;
    }


    // Getters and Setters
    public IOException getException() {
        return exception;
    }

    public List<MoviesModel> getPosters() {
        return posters;
    }

    public void setException(IOException exception) {
        this.exception = exception;
    }

    public void setPosters(List<MoviesModel> posters) {
        this.posters = posters;
    }
}
