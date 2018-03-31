package com.sh.study.udacitynano.popularmovies.service;

import android.os.AsyncTask;

import java.net.URL;
import java.util.ArrayList;

import com.sh.study.udacitynano.popularmovies.model.Movie;
import com.sh.study.udacitynano.popularmovies.network.NetworkUtils;

/**
 * AsyncTask used for import data from The Movies DB
 *
 * @author Sławomir Hagiel
 * @version 1.0
 * @since 2018-03-18
 */
public class FetchDataFromDBMoviesTask extends AsyncTask<Integer, Void, ArrayList<Movie>> {

    private static final String CLASS_NAME = FetchDataFromDBMoviesTask.class.getSimpleName();

    private OnEventListener<Boolean, ArrayList<Movie>> mProgressCallBack;

    public FetchDataFromDBMoviesTask(OnEventListener callback) {
        mProgressCallBack = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mProgressCallBack != null) mProgressCallBack.onProgress(true);
    }

    @Override
    protected ArrayList<Movie> doInBackground(Integer... integers) {

        URL moviesRequestUrl = NetworkUtils.buildUrl(integers[0]);

        try {
            String jsonDataResponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);

/*
        HttpResponse<String> response = Unirest.get("https://api.themoviedb.org/3/configuration?api_key=%3C%3Capi_key%3E%3E")
                .body("{}")
                .asString();
*/

            return NetworkUtils.getDataFromJson(jsonDataResponse);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> moviesData) {
        if (mProgressCallBack != null) mProgressCallBack.onProgress(false);
        if (moviesData != null) {
            if (mProgressCallBack != null) mProgressCallBack.onSuccess(moviesData);
        } else {
            if (mProgressCallBack != null) mProgressCallBack.onFailure();
        }
    }

}
