package com.example.cristianv.popularmovies.display.movie;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cristianv on 9/14/17.
 */

public class MovieModel {
    private String title;
    private String poster;
    private String overview;
    private String voteAverage;
    private String releaseDate;
    private String background;
    private String review;

    public MovieModel() {
    }

    public MovieModel(String title, String poster, String overview, String voteAverage, String releaseDate) {
        this.title = title;
        this.poster = poster;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    // Getters

    public String getTitle() {
        return title;
    }

    public String getPoster() {
        return poster;
    }

    public String getOverview() {
        return overview;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getBackground() {
        return background;
    }

    public String getReview() {
        return review;
    }

    // Setters

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setBackground(String background) { this.background = background; }

    public void setReview(String review) { this.review = review; }
}
