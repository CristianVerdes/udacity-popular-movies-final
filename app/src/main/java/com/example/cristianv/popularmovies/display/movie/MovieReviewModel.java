package com.example.cristianv.popularmovies.display.movie;

/**
 * Created by cristian on 11/20/17.
 */

public class MovieReviewModel {
    private String author;
    private String content;

    public MovieReviewModel(String author, String content) {
        this.author = author;
        this.content = content;
    }

    // GETTERS
    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

}
