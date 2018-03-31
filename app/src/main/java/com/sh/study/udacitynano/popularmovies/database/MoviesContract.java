package com.sh.study.udacitynano.popularmovies.database;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * A contract class for local database
 *
 * @author Sławomir Hagiel
 * @version 1.0
 * @since 2018-03-20
 *
 */
public class MoviesContract {
    static final String PROVIDER_NAME = "com.sh.study.udacitynano.popularmovies.provider";
    static final Uri URI_BASE_CONTENT = Uri.parse("content://" + PROVIDER_NAME);

    public static final class MoviesEntry implements BaseColumns {
        /**
         * Table movies
         */
        static final String TABLE_NAME = "movies";
        public static final String COL_MOVIE_ID = "movie_id";
        public static final String COL_VOTE_COUNT = "vote_count";
        public static final String COL_VIDEO = "video";
        public static final String COL_VOTE_AVERAGE = "vote_average";
        public static final String COL_POPULARITY = "popularity";
        public static final String COL_TITLE = "title";
        public static final String COL_POSTER = "poster";
        public static final String COL_OVERVIEW = "overview";
        public static final String COL_RELEASE_DATE = "release_date";
        public static final String COL_BACKDROP = "backdrop";
        public static final String COL_ORIGINAL_TITLE = "original_title";
        /**
         * Column update data about movies in local database from time to time
         */
        static final String COL_LAST_UPDATE_DATE = "last_update_date";
        /**
         * Column for store poster image for movie
         */
        static final String COL_POSTER_BLOB = "poster_blob";
        /**
         * Column for store poster image for movie
         */
        static final String COL_BACKDROP_BLOB = "backdrop_blob";
        /**
         * SQL Statement to create the movies table
         */
        static final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_MOVIE_ID + " INTEGER NOT NULL, " +
                COL_VOTE_COUNT + " INTEGER NOT NULL," +
                COL_VIDEO + " INTEGER NOT NULL," +
                COL_VOTE_AVERAGE + " REAL NOT NULL," +
                COL_POPULARITY + " REAL NOT NULL," +
                COL_TITLE + " TEXT NOT NULL," +
                COL_POSTER + " TEXT NOT NULL," +
                COL_POSTER_BLOB + " BLOB," +
                COL_OVERVIEW + " TEXT NOT NULL," +
                COL_RELEASE_DATE + " TEXT NOT NULL," +
                COL_BACKDROP + " TEXT NOT NULL," +
                COL_BACKDROP_BLOB + " BLOB," +
                COL_ORIGINAL_TITLE + " TEXT NOT NULL," +
                COL_LAST_UPDATE_DATE + " INTEGER DEFAULT CURRENT_DATE," +
                // If we have Movie with ID in the DB we replace the old one.
                " UNIQUE (" + COL_MOVIE_ID + ") ON CONFLICT REPLACE);";
        /**
         * SQL statement to delete the table
         */
        static final String SQL_DELETE_MOVIES_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        /**
         *  SQL statement sort order Popular Movies
         */
        public static final String SQL_SORT_ORDER_POPULARITY = COL_POPULARITY + " DESC";
        /**
         *  SQL statement sort order Top Rated Movies
         */
        public static final String SQL_SORT_ORDER_VOTE_AVERAGE = COL_VOTE_AVERAGE + " DESC";
        /**
         * The content URI base for all movies table
         */
        public static final Uri URI_MY_MOVIE = URI_BASE_CONTENT.buildUpon()
                .appendPath(TABLE_NAME)
                .build();
        /**
         * The content URI base for single movie
         * @param id Movie's Id.
         */
        public static Uri singleMovieUri(long id) {
            return ContentUris.withAppendedId(URI_MY_MOVIE, id);
        }
    }
}