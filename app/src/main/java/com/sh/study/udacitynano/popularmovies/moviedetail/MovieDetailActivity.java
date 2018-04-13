package com.sh.study.udacitynano.popularmovies.moviedetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.sh.study.udacitynano.popularmovies.R;
import com.sh.study.udacitynano.popularmovies.constants.MoviesConstants;
import com.sh.study.udacitynano.popularmovies.model.Movie;

import java.util.UnknownFormatConversionException;

/**
 * An activity representing a single movie detail screen.
 *
 * @author Sławomir Hagiel
 * @version 1.0
 * @since 2018-02-23
 */
public class MovieDetailActivity extends AppCompatActivity {
    private static final String CLASS_NAME = "MovieDetailActivity";
    private int mReturnStatus = -1;
    private boolean mTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MoviesConstants.debugTag(CLASS_NAME, "onCreate:start");

        setContentView(R.layout.activity_movie_detail);

        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = findViewById(R.id.fab);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(MoviesConstants.MOVIE,
                    getIntent().getParcelableExtra(MoviesConstants.MOVIE));
            mTwoPane = getIntent().getBooleanExtra(MoviesConstants.TWO_PANE, false);

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MoviesConstants.debugTag(CLASS_NAME, "onClick");
                int popular = MovieDetailContactProvider.changeMovieStatus(
                        getContentResolver(),
                        (Movie) getIntent().getParcelableExtra(MoviesConstants.MOVIE),
                        null);

                switch (popular) {
                    case MovieDetailContactProvider.IS_POPULAR:
                        if (mReturnStatus == -1) mReturnStatus = 1;
                        else if (mReturnStatus == 2) mReturnStatus = -1;

                        fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.btn_star_big_on));
                        break;
                    case MovieDetailContactProvider.IS_NOT_POPULAR:
                        if (mReturnStatus == -1) mReturnStatus = 2;
                        else if (mReturnStatus == 1) mReturnStatus = -1;
                        fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.btn_star_big_off));
                        break;
                    default:
                        throw new UnknownFormatConversionException("Unknown problems");
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MoviesConstants.debugTag(CLASS_NAME, "onOptionsItemSelected:start - id: " + item.toString());
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if ((mReturnStatus > 0) && (!mTwoPane)) {
            Intent back = new Intent();
            back.putExtra("result", mReturnStatus);
            setResult(Activity.RESULT_OK, back);
            finish();
        }
    }
}