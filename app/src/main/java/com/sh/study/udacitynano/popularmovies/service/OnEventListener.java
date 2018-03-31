package com.sh.study.udacitynano.popularmovies.service;

/**
 * Listener for {@link FetchDataFromDBMoviesTask }
 *
 * @author Sławomir Hagiel
 * @version 1.0
 * @since 2018-03-15
 */
public interface OnEventListener<T,V> {
    void onProgress(T visible);
    void onSuccess(V movies);
    void onFailure();
}
