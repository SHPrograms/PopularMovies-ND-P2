package com.sh.study.udacitynano.popularmovies.moviedetail;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sh.study.udacitynano.popularmovies.R;
import com.sh.study.udacitynano.popularmovies.model.Review;

import java.util.List;

/**
 * Adapter for list of reviews
 *
 * @author Sławomir Hagiel
 * @version 1.0
 * @since 2018-04-12
 */
class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsAdapterViewHolder> {
    private List<Review> mReviews;

    public void setReviews(List<Review> trailers) {
        this.mReviews = trailers;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reviews_content, parent, false);
        return new ReviewsAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapterViewHolder holder, int position) {
        holder.mAuthorView.setText(String.valueOf(mReviews.get(position).getAuthor()));
        holder.mContentView.setText(String.valueOf(mReviews.get(position).getContent()));
    }

    @Override
    public int getItemCount() {
        if (mReviews == null) return 0;
        else return mReviews.size();
    }

        public class ReviewsAdapterViewHolder extends RecyclerView.ViewHolder {
        private final TextView mAuthorView;
        private final TextView mContentView;


        public ReviewsAdapterViewHolder(View itemView) {
            super(itemView);
            mAuthorView = itemView.findViewById(R.id.detail_author);
            mContentView = itemView.findViewById(R.id.detail_content);
        }
    }
}
