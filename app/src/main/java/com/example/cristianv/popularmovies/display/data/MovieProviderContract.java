package com.example.cristianv.popularmovies.display.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by cristian.verdes on 20.02.2018.
 */

public class MovieProviderContract {
    public static final String AUTHORITY = "com.example.cristianv.popularmovies";

    // Base content Uri
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Paths
    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
    }

}
