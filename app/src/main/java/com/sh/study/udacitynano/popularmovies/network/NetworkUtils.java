package com.sh.study.udacitynano.popularmovies.network;

import com.sh.study.udacitynano.popularmovies.BuildConfig;
import com.sh.study.udacitynano.popularmovies.constants.MoviesConstants;
import com.sh.study.udacitynano.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the The Movie DB servers.
 *
 * @author Sławomir Hagiel
 * @version 1.0
 * @since 2018-02-23
 */
 public final class NetworkUtils {
    private static final String CLASS_NAME = "NetworkUtils";

    public static final int POPULARITY_MOVIES = 1;
    public static final int TOP_RATED_MOVIES = 2;

    private static final String API_KEY_TEXT = "?api_key=";

    private static final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/";

    private static final String POPULARITY_BASE_URL = "popular";
    private static final String TOP_RATED_BASE_URL = "top_rated";

    private static final String SINGLE_POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    //"w92", "w154", "w185", "w342", "w500", "w780", or "original".
    private static final String SINGLE_POSTER_SIZE_URL = "w185";

    /**
     * This method is a copy from Udacity's Sunshine Application.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        MoviesConstants.debugTag(CLASS_NAME, "getResponseFromHttpUrl:start - URL: " + url.toString());

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * Builds the URL used to talk to the Movie DB server.
     *
     * @param baseUrlBy Sorting by popular or top rated movies.
     * @return The URL to use to query list of movies.
     */
    public static URL buildUrl(int baseUrlBy) {
        String url = null;

        if (baseUrlBy == POPULARITY_MOVIES) {
            url = MOVIES_BASE_URL + POPULARITY_BASE_URL + API_KEY_TEXT + BuildConfig.POPULAR_MOVIES_MY_MOVIE_DB_API_KEY;
        } else if (baseUrlBy == TOP_RATED_MOVIES) {
            url = MOVIES_BASE_URL + TOP_RATED_BASE_URL + API_KEY_TEXT + BuildConfig.POPULAR_MOVIES_MY_MOVIE_DB_API_KEY;
        }
      return checkUrl(url);
    }
    /**
     * Builds the URL used to talk to the Movie DB server.
     *
     * @param poster Part of url of single poster
     * @return The URL of backdropPath or posterPath for single movie.
     */
    public static URL buildUrlForPoster(String poster) {
        return checkUrl(SINGLE_POSTER_BASE_URL + SINGLE_POSTER_SIZE_URL + poster);
    }

    /**
     * This method parses JSON from a web response and returns an array of Strings
     * describing the Grid of movies.
     *
     * @param movieJsonStr JSON response from server
     * @return Array of Strings describing Movies
     */
    public static ArrayList<Movie> getDataFromJson(String movieJsonStr) {
        MoviesConstants.debugTag(CLASS_NAME, "getDataFromJson:start");

        ArrayList<Movie> movies = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(movieJsonStr);
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject movie = jsonArray.getJSONObject(i);

                Movie m = Movie.create(
                        movie.getLong("id"),
                        movie.getLong("vote_count"),
                        movie.getBoolean("video"),
                        movie.getDouble("vote_average"),
                        movie.getDouble("popularity"),
                        movie.getString("title"),
                        movie.getString("poster_path"),
                        movie.getString("overview"),
                        movie.getString("release_date"),
                        movie.getString("backdrop_path"),
                        movie.getString("original_title"));
                movies.add(m);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

    /**
     * This method parses JSON from a web response and returns an array of Strings
     * describing the Grid of movies.
     *
     * @param url String to check
     * @return Correct URL
     * @throws MalformedURLException If URL is incorrect.
     */
    private static URL checkUrl(String url) {
        //MoviesConstants.debugTag(CLASS_NAME, "checkUrl: " + url);

        URL u = null;
        try {
            u = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return u;
    }
}