package com.example.cristianv.popularmovies.display.movies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cristianv.popularmovies.R;
import com.example.cristianv.popularmovies.display.HelpActivity;
import com.example.cristianv.popularmovies.display.data.MovieProviderContract;
import com.example.cristianv.popularmovies.display.movie.MovieActivity;

import java.util.List;

public class MoviesActivity extends AppCompatActivity implements ListItemClickListener, MoviesContract.View, LoaderManager.LoaderCallbacks<Cursor>, ListItemOnLongClickListener {
    private final String TAG = MovieActivity.class.getSimpleName();
    private static final int TASK_LOADER_ID = 0;


    private RecyclerView moviesList;
    private ProgressBar progressBar;
    private Toast toast;
    private MoviesContract.Presenter presenter;
    private MoviesAdapter moviesAdapter;
    private GridLayoutManager gridLayoutManager;
    private View noConection;


    private final int SPAN_COUNT = 3;
    private static final String KEY_FILTER = "key_filter";
    private static final String KEY_SCROLL_INDEX = "key_scroll_index";
    private static final String KEY_SCROLL_OFFSET = "key_scroll_offset";

    private MoviesPresenter.Filter filter;
    private int listScrollIndex;
    private int listScrollOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Views
        moviesList = (RecyclerView) findViewById(R.id.rv_movies);
        progressBar = (ProgressBar) findViewById(R.id.pb_loading_movie_data);
        noConection = (View) findViewById(R.id.no_connection);

        gridLayoutManager = new GridLayoutManager(this, SPAN_COUNT);
        moviesList.setLayoutManager(gridLayoutManager);
        moviesList.setHasFixedSize(true);
        moviesAdapter = new MoviesAdapter(this, this);
        moviesList.setAdapter(moviesAdapter);

        toast = new Toast(this);

        // Presenter
        presenter = new MoviesPresenter(this, new MoviesInteractor());

        if(savedInstanceState != null){
            filter = (MoviesPresenter.Filter) savedInstanceState.getSerializable(KEY_FILTER);
        } else {
            filter = MoviesPresenter.Filter.POPULAR;
        }

        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // I did this because the Presenter doesn't know the Loader
        if (filter.equals(MoviesPresenter.Filter.FAVORITES)) {
            getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
        } else {
            presenter.onResume(filter);
        }

        moviesList.smoothScrollToPosition(listScrollIndex);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
        getSupportLoaderManager().destroyLoader(TASK_LOADER_ID);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(KEY_FILTER, filter);
//        listState = gridLayoutManager.onSaveInstanceState();
//        outState.putParcelable("listState", listState);

        int scrollIndex = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
        outState.putInt(KEY_SCROLL_INDEX, scrollIndex);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null){
            filter = (MoviesPresenter.Filter) savedInstanceState.getSerializable(KEY_FILTER);
            //listState = savedInstanceState.getParcelable("listState");
            listScrollIndex = savedInstanceState.getInt(KEY_SCROLL_INDEX);
            listScrollOffset = savedInstanceState.getInt(KEY_SCROLL_OFFSET);
        } else {
            filter = MoviesPresenter.Filter.POPULAR;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sort_popular:
                getSupportActionBar().setTitle(R.string.popular_movies);
                presenter.onPopularSelected();
                filter = MoviesPresenter.Filter.POPULAR;
                return true;
            case R.id.sort_top_rated:
                getSupportActionBar().setTitle(R.string.top_rated_movies);
                presenter.onTopRatedSelected();
                filter = MoviesPresenter.Filter.TOP_RATED;
                return true;
            case R.id.sort_favorites:
                getSupportActionBar().setTitle(R.string.favorites);
                getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
                filter = MoviesPresenter.Filter.FAVORITES;
                return true;
            case R.id.help:
                HelpActivity.start(this);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // ListItemClickListener function
    @Override
    public void onListItemClick(MoviesModel moviesModel) {
        MovieActivity.start(this, moviesModel);
    }

    // ListItemOnLongClickListener
    @Override
    public void onLongListItemClick(String movieId) {
        // DELETE ITEM - Only for Favorites
        if (filter.equals(MoviesPresenter.Filter.FAVORITES)) {
            Toast.makeText(this, "Movie Deleted", Toast.LENGTH_SHORT).show();
            Uri uri = MovieProviderContract.MovieEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(movieId).build();

            getContentResolver().delete(uri, null, null);

            getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
            moviesAdapter.notifyDataSetChanged();
        }
    }

    // Movie View
    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        moviesList.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
        moviesList.setVisibility(View.VISIBLE);
    }

    @Override
    public void setItems(List<MoviesModel> items) {
        moviesAdapter.setItems(items);
        moviesAdapter.setNumberOfMovies(items.size());
        moviesAdapter.notifyDataSetChanged();

        gridLayoutManager.scrollToPositionWithOffset(listScrollIndex, 0);
    }



    @Override
    public void showNoConnection() {
        moviesAdapter.clearList();
        noConection.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoConnection() {
        noConection.setVisibility(View.INVISIBLE);
    }

    // LoaderManager
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new android.support.v4.content.AsyncTaskLoader<Cursor>(this) {
            Cursor movieData = null;

            @Override
            protected void onStartLoading() {
                if (movieData != null) {
                    deliverResult(movieData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return getContentResolver().query(MovieProviderContract.MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);
                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(Cursor data) {
                movieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        moviesAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        moviesAdapter.swapCursor(null);
    }


}
