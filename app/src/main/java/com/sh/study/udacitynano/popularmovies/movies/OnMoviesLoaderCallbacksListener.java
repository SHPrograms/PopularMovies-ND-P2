package com.sh.study.udacitynano.popularmovies.movies;

/**
 * Listener for {@link MoviesLoaderCallbacks }
 *
 * @author Sławomir Hagiel
 * @version 1.0
 * @since 2018-03-31
 */
public interface OnMoviesLoaderCallbacksListener<T,V> {
    void onProgress(T visible);
    void onLoadFinished(V data);
    void onLoaderReset();
}
