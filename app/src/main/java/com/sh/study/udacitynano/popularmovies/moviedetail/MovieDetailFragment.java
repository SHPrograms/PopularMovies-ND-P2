package com.sh.study.udacitynano.popularmovies.moviedetail;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sh.study.udacitynano.popularmovies.R;
import com.sh.study.udacitynano.popularmovies.Utils.GridAutoFitLayoutManager;
import com.sh.study.udacitynano.popularmovies.constants.MoviesConstants;
import com.sh.study.udacitynano.popularmovies.model.Movie;
import com.sh.study.udacitynano.popularmovies.model.Trailer;
import com.sh.study.udacitynano.popularmovies.network.NetworkUtils;
import com.squareup.picasso.Picasso;

/**
 * A fragment representing a single movie detail screen.
 *
 * @author Sławomir Hagiel
 * @version 1.0
 * @since 2018-02-23
 */
public class MovieDetailFragment extends Fragment implements TrailersAdapter.TrailersAdapterOnClickHandler {

    // TODO: RecyclerView sample in fragment https://developer.android.com/samples/RecyclerView/src/com.example.android.recyclerview/RecyclerViewFragment.html

    private static final String CLASS_NAME = "MovieDetailFragment";

    private Movie mMovie;
    private MovieDetailViewModel mViewModel;
    private TrailersAdapter mTrailersAdapter;
    private RecyclerView mTrailersRecyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MoviesConstants.debugTag(CLASS_NAME, "onCreate:start");

        if (getArguments().containsKey(MoviesConstants.MOVIE)) {
            mMovie = getArguments().getParcelable(MoviesConstants.MOVIE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MoviesConstants.debugTag(CLASS_NAME, "onCreateView:start");

        View rootView = inflater.inflate(R.layout.movie_detail, container, false);

        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
        FloatingActionButton appfab = activity.findViewById(R.id.fab);

        mTrailersRecyclerView = rootView.findViewById(R.id.detail_trailers_recycler);
        assert mTrailersRecyclerView != null;

        GridAutoFitLayoutManager trailerLayoutManager = new GridAutoFitLayoutManager(getContext(), 0);
        mTrailersRecyclerView.setLayoutManager(trailerLayoutManager);

        mTrailersAdapter = new TrailersAdapter(this);
        mTrailersRecyclerView.setAdapter(mTrailersAdapter);

/*
                RecyclerView mReviewsRecyclerView;
                mReviewsRecyclerView = rootView.findViewById(R.id.detail_reviews_recycler);
                assert mReviewsRecyclerView != null;
                LinearLayoutManager reviewsLayoutManager = new LinearLayoutManager(getContext());
                mTrailersRecyclerView.setLayoutManager(reviewsLayoutManager);
*/



        if (mMovie != null) {
            if (appBarLayout != null) {
                ImageView imageView = activity.findViewById(R.id.image_iv);
                appBarLayout.setTitle(mMovie.originalTitle());
                appBarLayout.setCollapsedTitleGravity(View.TEXT_ALIGNMENT_GRAVITY);
                appBarLayout.setCollapsedTitleTextAppearance(R.style.TextAppearance_AppCompat_Small);
                appBarLayout.setExpandedTitleTextAppearance(R.style.TextAppearance_AppCompat_Medium);

                Picasso.with(imageView.getContext())
                        .load(String.valueOf(NetworkUtils.buildUrlForPoster(mMovie.backdropPath())))
                        .into(imageView);
            } else {
                activity.setTitle(mMovie.originalTitle());
            }
            if (appfab != null) {
                if (MovieDetailContactProvider.isMovieInDB(activity.getContentResolver(), mMovie)) {
                    appfab.setImageDrawable(ContextCompat.getDrawable(activity.getApplicationContext(), android.R.drawable.star_big_on));
                }
            }
            ((TextView) rootView.findViewById(R.id.detail_overview_value_tv)).setText(mMovie.overview());
            ((TextView) rootView.findViewById(R.id.detail_vote_average_value_tv)).setText(String.valueOf(mMovie.voteAverage()));
            ((TextView) rootView.findViewById(R.id.detail_release_date_value_tv)).setText(mMovie.releaseDate());

            mViewModel = ViewModelProviders.of(this).get(MovieDetailViewModel.class);
            mViewModel.getTrailers(Long.toString(mMovie.id())).observe(this, trailers -> {
                mTrailersAdapter.setTrailers(trailers);
                for (Trailer trailer : trailers) {
                    MoviesConstants.debugTag(CLASS_NAME, "onCreateView: trailer: " + trailer.getName());
                }
            });

            // TODO: If no trailers then hide
//                    rootView.findViewById(R.id.detail_trailers_list).setVisibility(View.GONE);
//                    rootView.findViewById(R.id.detail_trailers_caption_tv).setVisibility(View.GONE);

            // TODO: If no reviews then hide
            rootView.findViewById(R.id.detail_reviews_recycler).setVisibility(View.GONE);
            rootView.findViewById(R.id.detail_reviews_caption_tv).setVisibility(View.GONE);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MoviesConstants.debugTag(CLASS_NAME, "onActivityCreated:start");
        // TODO: access to ViewModel here?
    }

    @Override
    public void onClick(Trailer trailer) {
        // TODO: run trailer
    }
}
