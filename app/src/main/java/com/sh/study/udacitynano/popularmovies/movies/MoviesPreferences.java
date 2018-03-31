package com.sh.study.udacitynano.popularmovies.movies;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Collected preferences for movies activity
 *
 * @author Sławomir Hagiel
 * @version 1.0
 * @since 2018-03-30
 */
final class MoviesPreferences {
    private MoviesPreferences() {
        throw new AssertionError();
    }
    static final String SOURCE = "source";
    static final int SOURCE_POPULAR_MOVIES = 1;
    static final int SOURCE_NOT_POPULAR_MOVIES = 2;

    static final String SORT = "sort";
    static final int SORT_POPULARITY_MOVIES = 1;
    static final int SORT_TOP_RATED_MOVIES = 2;

    static int getSourceMoviesPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(SOURCE, SOURCE_NOT_POPULAR_MOVIES);
    }

    static void setSourceMoviesPreferences(Context context, int value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putInt(SOURCE, value)
                .apply();
    }

    static int getSortMoviesPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(SORT, SORT_POPULARITY_MOVIES);
    }
    static void setSortMoviesPreferences(Context context, int value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putInt(SORT, value)
                .apply();
    }
}
