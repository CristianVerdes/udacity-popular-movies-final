package com.example.cristianv.popularmovies.display.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by cristian.verdes on 20.02.2018.
 */

public class MovieContentProvider extends ContentProvider {

    private DatabaseHelper databaseHelper;

    public static final int MOVIES = 100;
    public static final int MOVIE_WITH_ID = 101;

    public static final UriMatcher uriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MovieProviderContract.AUTHORITY, MovieProviderContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MovieProviderContract.AUTHORITY, MovieProviderContract.PATH_MOVIES + "/#", MOVIE_WITH_ID);

        return uriMatcher;
    }

    // ContentProvider Interface
    @Override
    public boolean onCreate() {
        Context context = getContext();
        databaseHelper = new DatabaseHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = databaseHelper.getReadableDatabase();
        int match = uriMatcher.match(uri);

        Cursor returnCursor;
        switch (match) {
            case MOVIES:
                returnCursor = db.query(DatabaseHelper.TABLE_FAVORITE_MOVIES,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);

        Uri returnUri;

        switch (match) {
            case MOVIES:
                long id = db.insert(DatabaseHelper.TABLE_FAVORITE_MOVIES,null , values);
                if (id > 0) {
                    // success
                    returnUri = ContentUris.withAppendedId(MovieProviderContract.MovieEntry.CONTENT_URI, id);
                } else {
                    // failure
                    returnUri = Uri.parse("");
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        int taskDeleted;

        switch (match) {
            case MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);

                taskDeleted = db.delete(DatabaseHelper.TABLE_FAVORITE_MOVIES, "movieId=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (taskDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return taskDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
