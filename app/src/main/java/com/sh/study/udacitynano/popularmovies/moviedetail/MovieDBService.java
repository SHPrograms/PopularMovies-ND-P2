package com.sh.study.udacitynano.popularmovies.moviedetail;

import com.sh.study.udacitynano.popularmovies.model.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Network calls
 *
 * @author Sławomir Hagiel
 * @version 1.0
 * @since 2018-04-10
 */
public interface MovieDBService {
    @GET("movie/{movie_id}/videos")
    Call<TrailerResponse> getTrailers(@Path("movie_id") String movieId, @Query("api_key") String apiKey);
}
