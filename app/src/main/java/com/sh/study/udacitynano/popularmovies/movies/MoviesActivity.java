package com.sh.study.udacitynano.popularmovies.movies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import com.facebook.stetho.Stetho;
import com.sh.study.udacitynano.popularmovies.R;
import com.sh.study.udacitynano.popularmovies.constants.MoviesConstants;
import com.sh.study.udacitynano.popularmovies.model.Movie;
import com.sh.study.udacitynano.popularmovies.moviedetail.MovieDetailActivity;
import com.sh.study.udacitynano.popularmovies.moviedetail.MovieDetailFragment;
import com.sh.study.udacitynano.popularmovies.service.FetchDataFromDBMoviesTask;
import com.sh.study.udacitynano.popularmovies.service.OnEventListener;


/**
 * An activity representing a grid of movies.
 *
 * @author Sławomir Hagiel
 * @version 1.0
 * @since 2018-02-23
 * <p>
 * Application is created on master - detail flow template accessible in Android Studio Environment
 * Some structures and comments are borrowed from {@see "https://github.com/udacity/ud851-Sunshine"}
 */
public class MoviesActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler {

    /**
     * TODO: Theme generate Render problem...
     * Options:
     * - Check {@see https://developer.android.com/guide/topics/ui/look-and-feel/themes.html} and
     *      create theme from the beginning.
     * - Go back to previous project configuration
     */


    private static final String CLASS_NAME = MoviesActivity.class.getSimpleName();
    private MoviesAdapter mMoviesAdapter;
    private ProgressBar mLoadingDataFromDBMoviesIndicator;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
          TODO: savedInstanceState doesn't work when orientation is changed in details and...
          use AsyncTaskLoader for hold position
         */


        MoviesConstants.debugTag(CLASS_NAME, "onCreate");

        setContentView(R.layout.activity_movie_list);

        Stetho.initializeWithDefaults(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
        }

        RecyclerView mRecyclerView = findViewById(R.id.movie_list);
        assert mRecyclerView != null;

        GridAutoFitLayoutManager layoutManager = new GridAutoFitLayoutManager(this, 0);
        mRecyclerView.setLayoutManager(layoutManager);

        mMoviesAdapter = new MoviesAdapter(this, new ArrayList<Movie>(), mTwoPane, this);
        mRecyclerView.setAdapter(mMoviesAdapter);

        mLoadingDataFromDBMoviesIndicator = findViewById(R.id.pbFetchDataFromDBMovies);


        if (MoviesPreferences.getSourceMoviesPreferences(this) == MoviesPreferences.SOURCE_POPULAR_MOVIES) {
            // TODO: Show movies from local db.

            // TODO: Temporary
            fetchDataFromDBMovies(MoviesPreferences.getSortMoviesPreferences(this));
        } else {
            fetchDataFromDBMovies(MoviesPreferences.getSortMoviesPreferences(this));
        }
    }

    /**
     * Starts the activity with the details of the chosen movie.
     *
     * @param movie Movie's details.
     */
    @Override
    public void onClick(Movie movie) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(MoviesConstants.MOVIE, movie);

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();
        } else {
            Context context = this;
            Intent intent = new Intent(context, MovieDetailActivity.class);
            intent.putExtra(MoviesConstants.MOVIE, movie);
            context.startActivity(intent);
        }
    }

    /**
     * Inflates menu where user can change sorting or show favourite movies.
     *
     * @param menu Menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_sorting_item:
                if (MoviesPreferences.getSortMoviesPreferences(this) == MoviesPreferences.SORT_POPULARITY_MOVIES) {
                    // TODO: Add second parameter - are we check movies from local db or from The Movie DB?

                    fetchDataFromDBMovies(MoviesPreferences.SORT_TOP_RATED_MOVIES);
                    item.setTitle(R.string.most_popular_action_item);
                    MoviesPreferences.setSortMoviesPreferences(this, MoviesPreferences.SORT_TOP_RATED_MOVIES);
                } else {
                    // TODO: Add second parameter - are we check movies from local db or from The Movie DB?

                    fetchDataFromDBMovies(MoviesPreferences.SORT_POPULARITY_MOVIES);
                    item.setTitle(R.string.top_rated_action_item);
                    MoviesPreferences.setSortMoviesPreferences(this, MoviesPreferences.SORT_POPULARITY_MOVIES);
                }
                return true;
            case R.id.menu_popular:
                if (MoviesPreferences.getSourceMoviesPreferences(this) == MoviesPreferences.SOURCE_POPULAR_MOVIES) {
                    // TODO: Show Not popular movies.

                    MoviesPreferences.setSourceMoviesPreferences(this, MoviesPreferences.SOURCE_NOT_POPULAR_MOVIES);
                } else {
                    // TODO: Show Popular movies from local db.

                    MoviesPreferences.setSourceMoviesPreferences(this, MoviesPreferences.SOURCE_POPULAR_MOVIES);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void fetchDataFromDBMovies(int sort) {
        new FetchDataFromDBMoviesTask(new OnEventListener<Boolean, ArrayList<Movie>>() {
            @Override
            public void onProgress(Boolean visible) {
                if (visible) mLoadingDataFromDBMoviesIndicator.setVisibility(View.VISIBLE);
                else mLoadingDataFromDBMoviesIndicator.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onSuccess(ArrayList<Movie> movies) {
                mMoviesAdapter.loadMovies(movies);
            }

            @Override
            public void onFailure() {
                Toast.makeText(MoviesActivity.this, R.string.No_internet_connection, Toast.LENGTH_LONG).show();
            }
        }).execute(sort);
    }
}
