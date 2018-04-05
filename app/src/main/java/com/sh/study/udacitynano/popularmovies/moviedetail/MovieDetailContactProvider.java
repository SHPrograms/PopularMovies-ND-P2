package com.sh.study.udacitynano.popularmovies.moviedetail;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.sh.study.udacitynano.popularmovies.constants.MoviesConstants;
import com.sh.study.udacitynano.popularmovies.database.MoviesContract;
import com.sh.study.udacitynano.popularmovies.model.Movie;

final class MovieDetailContactProvider {
    private static final String CLASS_NAME = "MovieDetailContactProvider";
    static final int IS_POPULAR = 1;
    static final int IS_NOT_POPULAR = 2;
    static final int IS_ERROR = 3;

    static boolean isMovieInDB(ContentResolver contentResolver, Movie movie) {
        MoviesConstants.debugTag(CLASS_NAME, "isMovieInDB:start - movieId: " + movie.id());
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(
                    MoviesContract.MoviesEntry.singleMovieUri(movie.id()),
                    null, null, null, null);

            return ((cursor != null) && (cursor.getCount() > 0));
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    static int changeMovieStatus(ContentResolver contentResolver, Movie movie, Boolean popular) {
        MoviesConstants.debugTag(CLASS_NAME, "changeMovieStatus:start - movieId: " + movie.id());
        if (popular == null) {
            popular = isMovieInDB(contentResolver, movie);
        }
        if (popular) {
            // delete from database
            int deleted = contentResolver.delete(
                    MoviesContract.MoviesEntry.singleMovieUri(movie.id()),
                    null, null);
            MoviesConstants.infoTag(contentResolver.getClass().getSimpleName(), "Deleted rows: " + String.valueOf(deleted));
            if (deleted > 0) return IS_NOT_POPULAR;
            else return IS_ERROR;
        } else {
            // insert to database
            ContentValues values = new ContentValues();
            values.clear();
            values.put(MoviesContract.MoviesEntry.COL_MOVIE_ID, movie.id());
            values.put(MoviesContract.MoviesEntry.COL_VOTE_COUNT, movie.voteCount());
            values.put(MoviesContract.MoviesEntry.COL_VIDEO, movie.video());
            values.put(MoviesContract.MoviesEntry.COL_VOTE_AVERAGE, movie.voteAverage());
            values.put(MoviesContract.MoviesEntry.COL_POPULARITY, movie.popularity());
            values.put(MoviesContract.MoviesEntry.COL_TITLE, movie.tittle());
            values.put(MoviesContract.MoviesEntry.COL_POSTER, movie.posterPath());
            values.put(MoviesContract.MoviesEntry.COL_OVERVIEW, movie.overview());
            values.put(MoviesContract.MoviesEntry.COL_RELEASE_DATE, movie.releaseDate());
            values.put(MoviesContract.MoviesEntry.COL_BACKDROP, movie.backdropPath());
            values.put(MoviesContract.MoviesEntry.COL_ORIGINAL_TITLE, movie.originalTitle());

            Uri insertResult = contentResolver.insert(
                    MoviesContract.MoviesEntry.singleMovieUri(movie.id()),
                    values);
            MoviesConstants.infoTag(contentResolver.getClass().getSimpleName(), "Inserted data: " + String.valueOf(insertResult));
            if (insertResult != null) return IS_POPULAR;
            else return IS_ERROR;
        }
    }
}
