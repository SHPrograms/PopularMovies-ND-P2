package com.sh.study.udacitynano.popularmovies.model;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;

/**
 * An Abstract class used to store data from The Movie DB JSON data.
 * Use Google's AutoValue library to auto-create classes
 *
 * @author Sławomir Hagiel
 * @version 1.0
 * @since 2018-02-25
 */
@AutoValue public abstract class Movie implements Parcelable {

    public abstract Long id();
    public abstract Long voteCount();
    public abstract Boolean video();
    public abstract Double voteAverage();
    public abstract Double popularity();
    public abstract String tittle();
    public abstract String posterPath();
    public abstract String overview();
    public abstract String releaseDate();
    public abstract String backdropPath();
    public abstract String originalTitle();

    public static Movie create(
            Long id,
            Long voteCount,
            Boolean video,
            Double voteAverage,
            Double popularity,
            String tittle,
            String posterPath,
            String overview,
            String releaseDate,
            String backdropPath,
            String originalTitle
    ) {
      return new AutoValue_Movie(
              id,
              voteCount,
              video,
              voteAverage,
              popularity,
              tittle,
              posterPath,
              overview,
              releaseDate,
              backdropPath,
              originalTitle
      );
    }
}