package com.sh.study.udacitynano.popularmovies.moviedetail;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sh.study.udacitynano.popularmovies.R;
import com.sh.study.udacitynano.popularmovies.constants.MoviesConstants;
import com.sh.study.udacitynano.popularmovies.model.Movie;
import com.sh.study.udacitynano.popularmovies.network.NetworkUtils;
import com.squareup.picasso.Picasso;

/**
 * A fragment representing a single movie detail screen.
 *
 * @author Sławomir Hagiel
 * @version 1.0
 * @since 2018-02-23
 */
public class MovieDetailFragment extends Fragment {
    private static final String CLASS_NAME = "MovieDetailFragment";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MoviesConstants.debugTag(CLASS_NAME, "onCreateView:start");

        View rootView = inflater.inflate(R.layout.movie_detail, container, false);

        if (getArguments().containsKey(MoviesConstants.MOVIE)) {
            Movie movie = getArguments().getParcelable(MoviesConstants.MOVIE);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            FloatingActionButton appfab = activity.findViewById(R.id.fab);

            if (movie != null) {
                if (appBarLayout != null) {
                    ImageView imageView = activity.findViewById(R.id.image_iv);
                    appBarLayout.setTitle(movie.originalTitle());
                    appBarLayout.setCollapsedTitleGravity(View.TEXT_ALIGNMENT_GRAVITY);
                    appBarLayout.setCollapsedTitleTextAppearance(R.style.TextAppearance_AppCompat_Small);
                    appBarLayout.setExpandedTitleTextAppearance(R.style.TextAppearance_AppCompat_Medium);

                    Picasso.with(imageView.getContext())
                            .load(String.valueOf(NetworkUtils.buildUrlForPoster(movie.backdropPath())))
                            .into(imageView);
                } else {
                    activity.setTitle(movie.originalTitle());
                }
                ((TextView) rootView.findViewById(R.id.detail_overview_value_tv)).setText(movie.overview());
                ((TextView) rootView.findViewById(R.id.detail_vote_average_value_tv)).setText(String.valueOf(movie.voteAverage()));
                ((TextView) rootView.findViewById(R.id.detail_release_date_value_tv)).setText(movie.releaseDate());

                if (appfab != null) {
                    if (MovieDetailContactProvider.isMovieInDB(activity.getContentResolver(), movie)) {
                        appfab.setImageDrawable(ContextCompat.getDrawable(activity.getApplicationContext(), android.R.drawable.star_big_on));
                    }
                }
            }
        }
        return rootView;
    }
}
