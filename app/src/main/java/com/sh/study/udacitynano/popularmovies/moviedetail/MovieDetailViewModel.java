package com.sh.study.udacitynano.popularmovies.moviedetail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.gson.GsonBuilder;
import com.sh.study.udacitynano.popularmovies.BuildConfig;
import com.sh.study.udacitynano.popularmovies.constants.MoviesConstants;
import com.sh.study.udacitynano.popularmovies.model.Trailer;
import com.sh.study.udacitynano.popularmovies.model.TrailerResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Detail Movie data imported from Movie DB
 *
 * @author Sławomir Hagiel
 * @version 1.0
 * @since 2018-04-10
 */
public class MovieDetailViewModel extends ViewModel {
    //TODO: More OOP
    private static final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/"; // without /movie because it I have in service

    private String mMovieId;
    private LiveData<List<Trailer>> mTrailers;
//    private LiveData<List<Review>> mReviews;

    public LiveData<List<Trailer>> getTrailers(String id) {
        mMovieId = id;

        final MutableLiveData<List<Trailer>> data = new MutableLiveData<>();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(MOVIES_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();

        MovieDBService service = retrofit.create(MovieDBService.class);

        Call<TrailerResponse> movieResultCallback = service.getTrailers(mMovieId, BuildConfig.POPULAR_MOVIES_MY_MOVIE_DB_API_KEY);
        movieResultCallback.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                // TODO: Do I need check codes?
                //int code = response.code();
                // can check the status code
                // status_code":34 - wrong url.
                data.setValue(response.body().getResults());
                MoviesConstants.debugTag("MovieDetailViewModel", response.body().getResults().toString());
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                MoviesConstants.errorTag("MovieDetailViewModel", t.toString());
            }
        });
        mTrailers = data;
        return data;
    }
}