package com.example.cristianv.popularmovies.display.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by cristian.verdes on 20.02.2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String LOG = "DatabaseHelper";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "moviesDatabase";

    public static final String TABLE_FAVORITE_MOVIES = "favMovies";

    // Columns
    private static final String KEY_ID = "_id";

    public static final String KEY_MOVIE_ID = "movieId";
    public static final String KEY_TITLE = "title";
    public static final String KEY_POSTER_PATH = "posterPath";

    // SQL Statements
    private static final String CREATE_TABLE_FAVORITE_MOVIES = "CREATE TABLE " +
            TABLE_FAVORITE_MOVIES + "(" + KEY_ID + " INTEGER PRIMARY KEY, "
                                        + KEY_MOVIE_ID + " INTEGER UNIQUE, "
                                        + KEY_TITLE + " TEXT, "
                                        + KEY_POSTER_PATH + " TEXT)";


    // SQLiteOpenHelper abstract class
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FAVORITE_MOVIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE_MOVIES);

        onCreate(db);
    }
}
