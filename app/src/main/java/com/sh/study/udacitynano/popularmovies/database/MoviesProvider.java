package com.sh.study.udacitynano.popularmovies.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.sh.study.udacitynano.popularmovies.constants.MoviesConstants;
import com.sh.study.udacitynano.popularmovies.database.MoviesContract.MoviesEntry;

/**
 * One of the project requirement for Popular Movies stage 2 is use ContentProvider Class.
 *
 * @author Sławomir Hagiel
 * @version 1.0
 * @since 2018-03-20
 */
public class MoviesProvider extends ContentProvider {

    private static final String CLASS_NAME = MoviesProvider.class.getSimpleName();

    public static final int CODE_MOVIES = 100;
    public static final int CODE_SINGLE_MOVIE = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(MoviesContract.PROVIDER_NAME, MoviesEntry.TABLE_NAME, CODE_MOVIES);
        sUriMatcher.addURI(MoviesContract.PROVIDER_NAME, MoviesEntry.TABLE_NAME + "/#", CODE_SINGLE_MOVIE);
    }

    private MoviesDBHelper mDBHelper;

    @Override
    public boolean onCreate() {
        MoviesConstants.debugTag(CLASS_NAME,"onCreate");

        mDBHelper = new MoviesDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        MoviesConstants.debugTag(CLASS_NAME,"query uri: " + uri.toString());

        switch (sUriMatcher.match(uri)) {
            case CODE_SINGLE_MOVIE: {
                // uri.getLastPathSegment() equals ContentUris.parseId(uri)
                selection = DatabaseUtils.concatenateWhere(
                        MoviesEntry.COL_MOVIE_ID + " = " + ContentUris.parseId(uri),
                        selection);
                break;
            }
            case CODE_MOVIES: {

                if (TextUtils.isEmpty(sortOrder)) {
                    // default sortOrder
                    sortOrder = MoviesEntry.SQL_SORT_ORDER_POPULARITY;
                } else {
                    // TODO: Add sorting by popular or ranking here or in MoviesActivity

                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        Cursor cursor = mDBHelper.getReadableDatabase().query(
                MoviesEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        MoviesConstants.debugTag(CLASS_NAME,"getType uri: " + uri.toString());
        // TODO: Provider getType implementation is not necessary for pass but use for return blob data...
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        MoviesConstants.debugTag(CLASS_NAME,"insert uri: " + uri.toString() + ", values: " + values.toString());

        Uri result;
        switch (sUriMatcher.match(uri)) {
            case CODE_SINGLE_MOVIE: {
                long id = mDBHelper.getWritableDatabase().insert(
                        MoviesEntry.TABLE_NAME,
                        null,
                        values);

                if (id > 0) {
                    result = MoviesEntry.singleMovieUri(id);
                    getContext().getContentResolver().notifyChange(result, null);
                } else {
                    throw new SQLException("Problem with insert data:" + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return result;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        MoviesConstants.debugTag(CLASS_NAME,"delete uri: " + uri.toString());

        int numberDeletedRows;
        switch (sUriMatcher.match(uri)) {
            case CODE_SINGLE_MOVIE: {
                numberDeletedRows = mDBHelper.getWritableDatabase().delete(
                        MoviesEntry.TABLE_NAME,
                        DatabaseUtils.concatenateWhere(
                                MoviesEntry.COL_MOVIE_ID + " = " + ContentUris.parseId(uri),
                                selection),
                        selectionArgs);

                if (numberDeletedRows > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                } else {
                    throw new SQLException("hmm, There is nothing to delete?:" + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return numberDeletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        MoviesConstants.debugTag(CLASS_NAME,"update uri: " + uri.toString() + ", values: " + values.toString());
        // TODO: Provider update implementation is not necessary for pass but use for regular...
        // update movies with old COL_LAST_UPDATE_DATE
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
