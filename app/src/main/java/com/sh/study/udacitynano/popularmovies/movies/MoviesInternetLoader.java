package com.sh.study.udacitynano.popularmovies.movies;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.sh.study.udacitynano.popularmovies.constants.MoviesConstants;
import com.sh.study.udacitynano.popularmovies.model.Movie;
import com.sh.study.udacitynano.popularmovies.network.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

/**
 * AsyncTaskLoader for for import data from The Movies DB
 * Replacing AsyncTask FetchDataFromDBMoviesTask from stage 1.
 *
 * @author Sławomir Hagiel
 * @version 1.0
 * @since 2018-04-05
 */
class MoviesInternetLoader extends AsyncTaskLoader<ArrayList<Movie>> {
    private static final String CLASS_NAME = "MoviesInternetLoader";
    private ArrayList<Movie> mMovies = null;

    MoviesInternetLoader(@NonNull Context context) {
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
        URL moviesRequestUrl;
        if (MoviesPreferences.getSortMoviesPreferences(getContext()) == MoviesPreferences.SORT_POPULARITY_MOVIES)
            moviesRequestUrl = NetworkUtils.buildUrl(NetworkUtils.POPULARITY_MOVIES);
        else
            moviesRequestUrl = NetworkUtils.buildUrl(NetworkUtils.TOP_RATED_MOVIES);

        try {
            String jsonDataResponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);
            return NetworkUtils.getDataFromJson(jsonDataResponse);

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
