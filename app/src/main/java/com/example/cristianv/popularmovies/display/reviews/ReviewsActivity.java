package com.example.cristianv.popularmovies.display.reviews;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.example.cristianv.popularmovies.R;
import com.example.cristianv.popularmovies.display.movie.MovieReviewModel;
import com.example.cristianv.popularmovies.display.movies.MoviesModel;
import com.example.cristianv.popularmovies.display.movies.MoviesPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cristian on 11/21/17.
 */

public class ReviewsActivity extends AppCompatActivity implements ReviewsContract.View,
                                                                ReviewsContract.ListItemClickListener{
    private int movieId;

    private RecyclerView reviewsList;

    private ReviewsAdapter reviewsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ReviewsPresenter reviewsPresenter;

    private static final String KEY_SCROLL_INDEX = "key_scroll_index";

    private int listScrollIndex;

    public static void start(Context context, int movieId) {
        Intent starter = new Intent(context, ReviewsActivity.class);
        starter.putExtra("movieId", movieId);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.more_reviews);

        reviewsList = findViewById(R.id.rv_reviews);
        setupRecyclerView();


        movieId = getIntent().getIntExtra("movieId", 0);

        reviewsPresenter = new ReviewsPresenter(this, new ReviewsInteractor(movieId));
    }

    @Override
    protected void onResume() {
        super.onResume();
        reviewsPresenter.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reviewsPresenter.onDestroy();
    }

    private void setupRecyclerView(){
        linearLayoutManager = new LinearLayoutManager(this);
        reviewsList.setLayoutManager(linearLayoutManager);
        reviewsList.setHasFixedSize(true);
        reviewsAdapter = new ReviewsAdapter(this);
        reviewsList.setAdapter(reviewsAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        int scrollIndex = linearLayoutManager.findFirstVisibleItemPosition();
        outState.putInt(KEY_SCROLL_INDEX, scrollIndex);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        listScrollIndex = savedInstanceState.getInt(KEY_SCROLL_INDEX);
    }

    // OnListItemClicked
    @Override
    public void onListItemClick(MovieReviewModel movieReviewModel) {
        // No need for a listener
    }

    // VIEW
    @Override
    public void showProgress() {
        //progressBar.setVisibility(View.VISIBLE);
        //moviesList.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideProgress() {
        //progressBar.setVisibility(View.INVISIBLE);
        //moviesList.setVisibility(View.VISIBLE);
    }

    @Override
    public void setItems(List<MovieReviewModel> reviews) {
        reviewsAdapter.setItems(reviews);
        reviewsAdapter.notifyDataSetChanged();

        // Reset scroll state
        reviewsList.smoothScrollToPosition(listScrollIndex);
    }

    @Override
    public void showNoConnection() {
        //moviesAdapter.clearList();
        //noConection.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoConnection() {
        //noConection.setVisibility(View.INVISIBLE);
    }

}
