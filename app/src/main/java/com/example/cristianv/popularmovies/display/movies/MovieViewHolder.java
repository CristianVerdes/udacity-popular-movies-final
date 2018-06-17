package com.example.cristianv.popularmovies.display.movies;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.cristianv.popularmovies.R;
import com.squareup.picasso.Picasso;

/**
 * Created by cristianv on 8/29/17.
 */

public class MovieViewHolder extends RecyclerView.ViewHolder{
    private ImageView displayMoviePoster;
    private ListItemClickListener listener;

    public MovieViewHolder(View itemView, ListItemClickListener listener) {
        super(itemView);

        this.displayMoviePoster = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
        this.listener = listener;
    }

    void bind(String photoPath){
        Picasso.with(itemView.getContext()).load(photoPath).into(displayMoviePoster);
    }
}
