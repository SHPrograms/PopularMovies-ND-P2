package com.sh.study.udacitynano.popularmovies.constants;

import android.util.Log;

/**
 * Collected constants used everywhere
 *
 * @author Sławomir Hagiel
 * @version 1.0
 * @since 2018-03-25
 */
public final class MoviesConstants {
    private MoviesConstants() {
        throw new AssertionError();
    }

    /*
     * LOG.d purposes
     */
    private static final String DEBUG_TAG = "PMDebugTag";
    /*
     * LOG.e purposes
     */
    private static final String ERROR_TAG = DEBUG_TAG;
    /*
     * LOG.i purposes
     */
    private static final String INFO_TAG = DEBUG_TAG;
    /*
     * LOG.d purposes
     */
    public static void debugTag(String name, String message) {
        Log.d(DEBUG_TAG, name + " - " + message);
    }
    /*
     * LOG.e purposes
     */
    public static void errorTag(String name, String message) {
        Log.d(ERROR_TAG, name + " - " + message);
    }
    /*
     * LOG.i purposes
     */
    public static void infoTag(String name, String message) {
        Log.d(INFO_TAG, name + " - " + message);
    }
    /*
     * An argument from {@link MoviesActivity} used in detail activity and fragment
     * representing the single movie data
     */
    public static final String MOVIE = "movie";
}
