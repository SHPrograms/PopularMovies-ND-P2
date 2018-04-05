package com.sh.study.udacitynano.popularmovies.movies;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.sh.study.udacitynano.popularmovies.constants.MoviesConstants;
import com.sh.study.udacitynano.popularmovies.model.Movie;

import java.util.ArrayList;

/**
 * LoaderManager.LoaderCallbacks for for import data from The Movies DB and local DB
 *
 * @author Sławomir Hagiel
 * @version 1.0
 * @since 2018-04-04
 */
public class MoviesLoaderCallbacks implements LoaderManager.LoaderCallbacks<ArrayList<Movie>> {
    private static final String CLASS_NAME = "MoviesLoaderCallbacks";
    private Context mContext;
    private OnMoviesLoaderCallbacksListener<Boolean, ArrayList<Movie>> mOnMoviesLoaderCallbacksListener;

    /**
     * Constructor
     *
     * @param context
     * @param onMoviesLoaderCallbacksListener
     */
    public MoviesLoaderCallbacks(Context context, OnMoviesLoaderCallbacksListener<Boolean, ArrayList<Movie>> onMoviesLoaderCallbacksListener) {
        this.mContext = context;
        this.mOnMoviesLoaderCallbacksListener = onMoviesLoaderCallbacksListener;
        MoviesConstants.debugTag(CLASS_NAME, "constructor:end");
    }

    /**
     * Implements LoaderManager.LoaderCallbacks
     *
     * @param id
     * @param args
     * @return
     */
    @NonNull
    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, @Nullable Bundle args) {
        MoviesConstants.debugTag(CLASS_NAME, "onCreateLoader:start");
        mOnMoviesLoaderCallbacksListener.onProgress(true);
        if (MoviesPreferences.getSourceMoviesPreferences(mContext) == MoviesPreferences.SOURCE_POPULAR_MOVIES)
            return new MoviesLocalLoader(mContext);
        else return new MoviesInternetLoader(mContext);
    }

    /**
     * Implements LoaderManager.LoaderCallbacks
     *
     * @param loader
     * @param data
     */
    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        MoviesConstants.debugTag(CLASS_NAME, "onLoadFinished:start");
        mOnMoviesLoaderCallbacksListener.onLoadFinished(data);
        mOnMoviesLoaderCallbacksListener.onProgress(false);
    }

    /**
     * Implements LoaderManager.LoaderCallbacks
     *
     * @param loader
     */
    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Movie>> loader) {
        MoviesConstants.debugTag(CLASS_NAME, "onLoaderReset:start");
        mOnMoviesLoaderCallbacksListener.onLoaderReset();
    }
}