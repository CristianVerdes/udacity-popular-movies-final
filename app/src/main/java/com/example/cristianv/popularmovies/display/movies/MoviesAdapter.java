package com.example.cristianv.popularmovies.display.movies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cristianv.popularmovies.R;
import com.example.cristianv.popularmovies.display.data.DatabaseHelper;
import com.example.cristianv.popularmovies.display.movie.MovieModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cristianv on 8/29/17.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MovieViewHolder> {
    private List<MoviesModel> items;
    private int numberOfMovies;
    private final ListItemClickListener onClickListener;
    private final ListItemOnLongClickListener onLongClickListener;

    public MoviesAdapter(ListItemClickListener onClickListener, ListItemOnLongClickListener onLongClickListener){
        this.onClickListener = onClickListener;
        this.onLongClickListener = onLongClickListener;
        this.items = new ArrayList<>();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.movies_list_item, parent, false);

        MovieViewHolder movieViewHolder = new MovieViewHolder(view, onClickListener);

        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, final int position) {
        holder.bind(items.get(position).getPhotoPath());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onListItemClick(items.get(position));
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String stringId = String.valueOf(items.get(position).getMovieId());
                onLongClickListener.onLongListItemClick(stringId);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return numberOfMovies;
    }

    public void clearList(){
        this.items.clear();
        numberOfMovies = 0;
        this.notifyDataSetChanged();
    }


    // Setters

    public void setItems(List<MoviesModel> items) {
        this.items = items;
    }

    public void setNumberOfMovies(int numberOfMovies) {
        this.numberOfMovies = numberOfMovies;
    }


    public void swapCursor(Cursor data) {
        if (data != null) {
            this.items.clear();
            List<MoviesModel> movies = new ArrayList<>();

            int posterColumnIndex = data.getColumnIndex(DatabaseHelper.KEY_POSTER_PATH);
            int movieIdColumnIndex = data.getColumnIndex(DatabaseHelper.KEY_MOVIE_ID);

            while (data.moveToNext()) {
                movies.add(new MoviesModel(data.getString(posterColumnIndex), data.getInt(movieIdColumnIndex)));
            }

            setItems(movies);
            setNumberOfMovies(movies.size());
            notifyDataSetChanged();
        }
    }
}
