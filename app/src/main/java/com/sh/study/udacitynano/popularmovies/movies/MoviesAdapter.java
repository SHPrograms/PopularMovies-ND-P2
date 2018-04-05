package com.sh.study.udacitynano.popularmovies.movies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.view.View.OnClickListener;

import com.sh.study.udacitynano.popularmovies.R;
import com.sh.study.udacitynano.popularmovies.model.Movie;
import com.sh.study.udacitynano.popularmovies.network.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Adapter for list of movies
 *
 * @author Sławomir Hagiel
 * @version 1.0
 * @since 2018-02-23
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    private ArrayList<Movie> movies;
    final private MoviesAdapterOnClickHandler mClickHandler;

    public interface MoviesAdapterOnClickHandler {
        void onClick(Movie movie);
    }

   public MoviesAdapter(ArrayList<Movie> movieList, MoviesAdapterOnClickHandler clickHandler) {
        movies = new ArrayList<>();
        if (movieList != null) {
            movies = movieList;
        }
        mClickHandler = clickHandler;
    }

    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_content, parent, false);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder holder, int position) {
        Picasso.with(holder.mPoster.getContext())
                .load(String.valueOf(NetworkUtils.buildUrlForPoster(movies.get(position).posterPath())))
                .into(holder.mPoster);
    }

    @Override
    public int getItemCount() {
        if (movies == null) return 0;
        else return movies.size();
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        private final ImageView mPoster;

        public MoviesAdapterViewHolder(View view) {
            super(view);
            mPoster = view.findViewById(R.id.movie_poster_iv);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movie = movies.get(adapterPosition);
            mClickHandler.onClick(movie);
        }
    }
    public void loadMovies(ArrayList<Movie> moviesData) {
        movies = moviesData;
        notifyDataSetChanged();
    }
}