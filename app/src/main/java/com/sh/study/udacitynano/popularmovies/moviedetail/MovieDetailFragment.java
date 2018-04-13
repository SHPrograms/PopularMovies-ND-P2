package com.sh.study.udacitynano.popularmovies.moviedetail;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
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
    private static final String CLASS_NAME = "MovieDetailFragment";

    private Movie mMovie;
    private MovieDetailViewModel mViewModel;
    private TrailersAdapter mTrailersAdapter;
    private RecyclerView mTrailersRecyclerView;
    private ReviewsAdapter mReviewsAdapter;
    private RecyclerView mReviewsRecyclerView;

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
        mReviewsRecyclerView = rootView.findViewById(R.id.detail_reviews_recycler);
        assert mTrailersRecyclerView != null;
        assert mReviewsRecyclerView != null;

        mTrailersRecyclerView.setLayoutManager(new GridAutoFitLayoutManager(getContext(), 0));
        mReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mTrailersAdapter = new TrailersAdapter(this);
        mReviewsAdapter = new ReviewsAdapter();

        mTrailersRecyclerView.setAdapter(mTrailersAdapter);
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);

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
                if (trailers.size() == 0) {
                    rootView.findViewById(R.id.detail_trailers_caption_tv).setVisibility(View.GONE);
                    rootView.findViewById(R.id.detail_trailers_recycler).setVisibility(View.GONE);
                }
            });
            mViewModel.getReviews(Long.toString(mMovie.id())).observe(this, reviews -> {
                mReviewsAdapter.setReviews(reviews);
                if (reviews.size() == 0) {
                    rootView.findViewById(R.id.detail_reviews_recycler).setVisibility(View.GONE);
                    rootView.findViewById(R.id.detail_reviews_caption_tv).setVisibility(View.GONE);
                }
            });
        }
        return rootView;
    }

    @Override
    public void onClick(Trailer trailer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.youtube_chooser_title)
                .setItems(R.array.pick_source, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = null;
                        switch (which) {
                            case 0:
                                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailer.getKey()));
                                break;
                            case 1:
                                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));
                                break;
                        }
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException ex) {
                            MoviesConstants.errorTag("Dialog", ex.getMessage());
                        }
                    }
                });
        builder.create();
        builder.show();
    }
}
