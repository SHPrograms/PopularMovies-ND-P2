package com.sh.study.udacitynano.popularmovies.movies;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.sh.study.udacitynano.popularmovies.constants.MoviesConstants;
import com.sh.study.udacitynano.popularmovies.database.MoviesContract;
import com.sh.study.udacitynano.popularmovies.model.Movie;

import java.util.ArrayList;

/**
 * AsyncTaskLoader for for import data from local DB
 *
 * @author Sławomir Hagiel
 * @version 1.0
 * @since 2018-04-05
 */
class MoviesLocalLoader extends AsyncTaskLoader<ArrayList<Movie>> {
    private static final String CLASS_NAME = "MoviesLocalLoader";
    private ArrayList<Movie> mMovies = null;

    MoviesLocalLoader(@NonNull Context context) {
        super(context);
        MoviesConstants.debugTag(CLASS_NAME, "Constructor:end");
    }

    @Override
    protected void onStartLoading() {
//        super.onStartLoading();
        MoviesConstants.debugTag(CLASS_NAME, "onStartLoading:start");
        if (mMovies != null) deliverResult(mMovies);
        else forceLoad();
    }

    @Nullable
    @Override
    public ArrayList<Movie> loadInBackground() {
        MoviesConstants.debugTag(CLASS_NAME, "loadInBackground:start");

        try {
            String sortOrder;
            if (MoviesPreferences.getSortMoviesPreferences(getContext()) == MoviesPreferences.SORT_POPULARITY_MOVIES)
                sortOrder = MoviesContract.MoviesEntry.SQL_SORT_ORDER_POPULARITY;
            else
                sortOrder = MoviesContract.MoviesEntry.SQL_SORT_ORDER_VOTE_AVERAGE;

            Cursor cursor = getContext().getContentResolver().query(
                    MoviesContract.MoviesEntry.URI_MY_MOVIE,
                    null,
                    null,
                    null,
                    sortOrder);

            mMovies = new ArrayList<>();

            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Movie m = Movie.create(
                        cursor.getLong(cursor.getColumnIndexOrThrow(MoviesContract.MoviesEntry.COL_MOVIE_ID)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(MoviesContract.MoviesEntry.COL_VOTE_COUNT)),
                        (cursor.getInt(cursor.getColumnIndexOrThrow(MoviesContract.MoviesEntry.COL_VIDEO)) == 1),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(MoviesContract.MoviesEntry.COL_VOTE_AVERAGE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(MoviesContract.MoviesEntry.COL_POPULARITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MoviesContract.MoviesEntry.COL_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MoviesContract.MoviesEntry.COL_POSTER)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MoviesContract.MoviesEntry.COL_OVERVIEW)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MoviesContract.MoviesEntry.COL_RELEASE_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MoviesContract.MoviesEntry.COL_BACKDROP)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MoviesContract.MoviesEntry.COL_ORIGINAL_TITLE))
                );
                mMovies.add(m);
            }
            cursor.close();
            return mMovies;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deliverResult(@Nullable ArrayList<Movie> data) {
        MoviesConstants.debugTag(CLASS_NAME, "deliverResult:start");
        mMovies = data;
        super.deliverResult(data);
    }
}

