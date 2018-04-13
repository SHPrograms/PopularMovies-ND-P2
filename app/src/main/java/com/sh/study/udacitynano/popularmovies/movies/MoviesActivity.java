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

import java.util.ArrayList;

import com.facebook.stetho.Stetho;
import com.sh.study.udacitynano.popularmovies.R;
import com.sh.study.udacitynano.popularmovies.Utils.GridAutoFitLayoutManager;
import com.sh.study.udacitynano.popularmovies.constants.MoviesConstants;
import com.sh.study.udacitynano.popularmovies.model.Movie;
import com.sh.study.udacitynano.popularmovies.moviedetail.MovieDetailActivity;
import com.sh.study.udacitynano.popularmovies.moviedetail.MovieDetailFragment;


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

    private static final String CLASS_NAME = "MoviesActivity";
    private static final int CUR_ID = 1001;
    private static final int CALLBACK_REQUEST = 1;
    private static final String IS_CHANGE = "result";

    private MoviesAdapter mMoviesAdapter;
    private ProgressBar mLoadingDataFromDBMoviesIndicator;
    private RecyclerView mRecyclerView;
    private boolean mTwoPane;
    private int mPosition;

    /**
     * Main activity create method.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MoviesConstants.debugTag(CLASS_NAME, "onCreate:start");
        setContentView(R.layout.activity_movie_list);
        Stetho.initializeWithDefaults(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
        }

        mRecyclerView = findViewById(R.id.movie_list);
        assert mRecyclerView != null;

        GridAutoFitLayoutManager layoutManager = new GridAutoFitLayoutManager(this, 0);
        mRecyclerView.setLayoutManager(layoutManager);

        mMoviesAdapter = new MoviesAdapter(new ArrayList<Movie>(), this);
        mRecyclerView.setAdapter(mMoviesAdapter);

        mLoadingDataFromDBMoviesIndicator = findViewById(R.id.pbFetchDataFromDBMovies);

        getSupportLoaderManager().initLoader(CUR_ID, null, getMoviesLoaderCallbacks());
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
            startActivityForResult(intent, CALLBACK_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!mTwoPane) {
            if (requestCode == CALLBACK_REQUEST) {
                if (MoviesPreferences.getSourceMoviesPreferences(this) == MoviesPreferences.SOURCE_POPULAR_MOVIES) {
                    mPosition = -1;
                    getSupportLoaderManager().destroyLoader(CUR_ID);
                    getSupportLoaderManager().initLoader(CUR_ID, null, getMoviesLoaderCallbacks());
                    mMoviesAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    /**
     * Inflates menu where user can change sorting or show favourite movies.
     *
     * @param menu Menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MoviesConstants.debugTag(CLASS_NAME, "onCreateOptionsMenu:start");
        getMenuInflater().inflate(R.menu.sort, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MoviesConstants.debugTag(CLASS_NAME, "onPrepareOptionsMenu:start");
        if (MoviesPreferences.getSortMoviesPreferences(this) == MoviesPreferences.SORT_TOP_RATED_MOVIES)
            menu.findItem(R.id.menu_sorting_item).setTitle(R.string.most_popular_action_item);
        else menu.findItem(R.id.menu_sorting_item).setTitle(R.string.top_rated_action_item);
        if (MoviesPreferences.getSourceMoviesPreferences(this) == MoviesPreferences.SOURCE_NOT_POPULAR_MOVIES)
            menu.findItem(R.id.menu_popular).setIcon(android.R.drawable.btn_star_big_off);
        else menu.findItem(R.id.menu_popular).setIcon(android.R.drawable.btn_star_big_on);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Menu item selected.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_sorting_item:
                if (MoviesPreferences.getSortMoviesPreferences(this) == MoviesPreferences.SORT_POPULARITY_MOVIES) {
                    MoviesPreferences.setSortMoviesPreferences(this, MoviesPreferences.SORT_TOP_RATED_MOVIES);
                    item.setTitle(R.string.most_popular_action_item);
                } else {
                    MoviesPreferences.setSortMoviesPreferences(this, MoviesPreferences.SORT_POPULARITY_MOVIES);
                    item.setTitle(R.string.top_rated_action_item);
                }
                mPosition = -1;
                getSupportLoaderManager().restartLoader(CUR_ID, null, getMoviesLoaderCallbacks());
                return true;
            case R.id.menu_popular:
                // star
                if (MoviesPreferences.getSourceMoviesPreferences(this) == MoviesPreferences.SOURCE_POPULAR_MOVIES) {
                    MoviesPreferences.setSourceMoviesPreferences(this, MoviesPreferences.SOURCE_NOT_POPULAR_MOVIES);
                    item.setIcon(android.R.drawable.btn_star_big_off);
                } else {
                    MoviesPreferences.setSourceMoviesPreferences(this, MoviesPreferences.SOURCE_POPULAR_MOVIES);
                    item.setIcon(android.R.drawable.btn_star_big_on);
                }
                mPosition = -1;
                getSupportLoaderManager().restartLoader(CUR_ID, null, getMoviesLoaderCallbacks());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Callback for LoaderManager
     *
     * @return callback
     */
    private MoviesLoaderCallbacks getMoviesLoaderCallbacks() {
        MoviesConstants.debugTag(CLASS_NAME, "MoviesLoaderCallbacks:start");
        return new MoviesLoaderCallbacks(this, new OnMoviesLoaderCallbacksListener<Boolean, ArrayList<Movie>>() {

            /**
             * Show or hide progress indicator.
             * Implements OnMoviesLoaderCallbacksListener.
             *
             * @param visible
             */
            @Override
            public void onProgress(Boolean visible) {
                MoviesConstants.debugTag(CLASS_NAME, "onProgress:start");
                if (visible) mLoadingDataFromDBMoviesIndicator.setVisibility(View.VISIBLE);
                else mLoadingDataFromDBMoviesIndicator.setVisibility(View.INVISIBLE);
            }

            /**
             * Load movies data to adapter.
             * Implements OnMoviesLoaderCallbacksListener.
             *
             * @param data
             */
            @Override
            public void onLoadFinished(ArrayList<Movie> data) {
                MoviesConstants.debugTag(CLASS_NAME, "onLoadFinished:start");
                mMoviesAdapter.loadMovies(data);

                if (mPosition > 0) {
                    mRecyclerView.smoothScrollToPosition(mPosition);
                }
            }

            /**
             *
             * Implements OnMoviesLoaderCallbacksListener
             */
            @Override
            public void onLoaderReset() {
                MoviesConstants.debugTag(CLASS_NAME, "onLoaderReset:start");
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        MoviesConstants.debugTag(CLASS_NAME, "onSaveInstanceState:start");
        outState.putInt("position", mPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        MoviesConstants.debugTag(CLASS_NAME, "onRestoreInstanceState:end");
        mPosition = savedInstanceState.getInt("position");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        MoviesConstants.debugTag(CLASS_NAME, "onPause:after super: " + mMoviesAdapter.getPosition());
        mPosition = mMoviesAdapter.getPosition();
    }
}

