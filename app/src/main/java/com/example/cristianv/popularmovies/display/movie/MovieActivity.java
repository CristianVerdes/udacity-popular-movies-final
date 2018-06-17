package com.example.cristianv.popularmovies.display.movie;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cristianv.popularmovies.R;
import com.example.cristianv.popularmovies.display.data.DatabaseHelper;
import com.example.cristianv.popularmovies.display.data.MovieProviderContract;
import com.example.cristianv.popularmovies.display.movies.MoviesModel;
import com.example.cristianv.popularmovies.display.reviews.ReviewsActivity;
import com.example.cristianv.popularmovies.utilities.NetworkUtils;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by cristianv on 9/5/17.
 */

public class MovieActivity extends AppCompatActivity implements MovieContract.View,
                                                                MovieContract.OnPlayVideoListener{
    private static final String TAG = MovieActivity.class.getSimpleName();
    private static String KEY_SCROLL_STATE = "scrollState";

    private ScrollView scrollView;
    private ImageView background;
    private ImageView poster;
    private TextView title;
    private TextView releaseDate;
    private TextView voteAverage;
    private TextView overview;
    private View movieDetails;
    private ProgressBar movieProgressBar;
    private ProgressBar backgroundProgressBar;
    private View noConnection;
    private MoviePresenter presenter;
    private TextView reviewAuthor;
    private TextView reviewContent;
    private View reviewLine;
    private TextView noReviews;
    private TextView moreReviews;

    private int movieId;
    private String moviePoster;
    private MovieModel movieModel;

    private VideosAdapter videosAdapter;

    private int scrollStateX;
    private int scrollStateY;


    public static void start(Context context, MoviesModel moviesModel) {
        Intent starter = new Intent(context, MovieActivity.class);
        starter.putExtra("movieId", moviesModel.getMovieId());
        starter.putExtra("moviePoster", moviesModel.getPhotoPath());
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        scrollView = findViewById(R.id.sv_parent);

        background = (ImageView) findViewById(R.id.iv_background);
        backgroundProgressBar = (ProgressBar) findViewById(R.id.pb_loading_background);

        poster = (ImageView) findViewById(R.id.iv_movie_poster);
        title = (TextView) findViewById(R.id.tv_title);
        releaseDate = (TextView) findViewById(R.id.tv_release_date);
        voteAverage = (TextView) findViewById(R.id.tv_vote_average);
        overview = (TextView) findViewById(R.id.tv_overview);

        movieDetails = (View) findViewById(R.id.movie_details);
        movieProgressBar = (ProgressBar) findViewById(R.id.pb_loading_movie_data);
        noConnection = (View) findViewById(R.id.no_connection);

        reviewAuthor = (TextView) findViewById(R.id.tv_review_author);
        reviewContent = (TextView) findViewById(R.id.tv_review_content);
        reviewLine = (View) findViewById(R.id.v_review_line);
        noReviews = (TextView) findViewById(R.id.tv_no_reviews);
        moreReviews = (TextView) findViewById(R.id.tv_more_reviews);

        moviePoster = getIntent().getStringExtra("moviePoster");
        movieId = getIntent().getIntExtra("movieId", 0);

        presenter = new MoviePresenter(this, new MovieInteractor(moviePoster, movieId));

        moreReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReviewsActivity.start(MovieActivity.this, movieId);
            }
        });

        // Prepare Videos Recycler View
        videosAdapter = new VideosAdapter(this, this);
        RecyclerView recyclerView = findViewById(R.id.rv_videos);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setAdapter(videosAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putIntArray(KEY_SCROLL_STATE, new int[]{scrollView.getScrollX(), scrollView.getScrollY()});

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int[] scrollState = savedInstanceState.getIntArray(KEY_SCROLL_STATE);
        if (scrollState != null) {
            scrollStateX = scrollState[0];
            scrollStateY = scrollState[1];
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_favorite, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                super.onBackPressed();
                return true;
            case R.id.add_favorite:
                onAddFavoritePressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onAddFavoritePressed() {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_MOVIE_ID, movieId);
        values.put(DatabaseHelper.KEY_TITLE, movieModel.getTitle());
        values.put(DatabaseHelper.KEY_POSTER_PATH, moviePoster);

        Uri uri = getContentResolver().insert(MovieProviderContract.MovieEntry.CONTENT_URI, values);

        if (uri != null && !uri.equals(Uri.parse(""))) {
            Toast.makeText(this, "Movie added to Favorites", Toast.LENGTH_SHORT).show();
            Log.d(MovieActivity.TAG, uri.toString());
        }
    }

    // View
    @Override
    public void showProgress() {
        movieDetails.setVisibility(View.GONE);
        overview.setVisibility(View.GONE);
        movieProgressBar.setVisibility(View.VISIBLE);
        backgroundProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        movieDetails.setVisibility(View.VISIBLE);
        overview.setVisibility(View.VISIBLE);
        movieProgressBar.setVisibility(View.GONE);
        backgroundProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void displayData(MovieModel movieModel) {
        this.movieModel = movieModel;
        hideProgress();
        getSupportActionBar().setTitle(movieModel.getTitle());

        Picasso.with(this).load(movieModel.getPoster()).into(poster);
        String backgroundUrl = NetworkUtils.createBackgroundLink(movieModel.getBackground());
        Picasso.with(this).load(backgroundUrl).into(background, new Callback() {
            @Override
            public void onSuccess() {
                backgroundProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });
        title.setText(movieModel.getTitle());
        releaseDate.setText(movieModel.getReleaseDate());
        voteAverage.setText(movieModel.getVoteAverage());
        overview.setText(movieModel.getOverview());


    }

    @Override
    public void displayData(MovieReviewModel movieReviewModel){
        noReviews.setVisibility(View.GONE);
        reviewAuthor.setVisibility(View.VISIBLE);
        reviewContent.setVisibility(View.VISIBLE);
        reviewLine.setVisibility(View.VISIBLE);
        moreReviews.setVisibility(View.VISIBLE);
        reviewAuthor.setText(movieReviewModel.getAuthor());
        reviewContent.setText(movieReviewModel.getContent());
    }

    @Override
    public void showNoReviews(){
        noReviews.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoConnection() {
        movieDetails.setVisibility(View.GONE);
        overview.setVisibility(View.GONE);
        movieProgressBar.setVisibility(View.GONE);
        noConnection.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoConnection() {
        movieDetails.setVisibility(View.VISIBLE);
        overview.setVisibility(View.VISIBLE);
        noConnection.setVisibility(View.GONE);
    }

    @Override
    public void showVideos(List<String> videos) {
        videosAdapter.setVideos(videos);

        // Restore Scroll State
        // scrollView.scrollTo(scrollStateX, scrollStateY);
        scrollView.smoothScrollTo(scrollStateX, scrollStateY);
    }

    // OnPlayVideoListener Interface
    @Override
    public void onPlayVideoClicked(String videoPath) {
        Intent intent = YouTubeStandalonePlayer.createVideoIntent((MovieActivity) this, NetworkUtils.YT_API_KEY, videoPath, 0, true, true);

        startActivity(intent);
    }
}
