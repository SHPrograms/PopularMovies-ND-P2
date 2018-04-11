package com.sh.study.udacitynano.popularmovies.moviedetail;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sh.study.udacitynano.popularmovies.R;
import com.sh.study.udacitynano.popularmovies.model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter for list of trailers
 *
 * @author Sławomir Hagiel
 * @version 1.0
 * @since 2018-04-10
 */
class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersAdapterViewHolder> {
    private List<Trailer> trailers;

    final private TrailersAdapterOnClickHandler mClickHandler;

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
        this.notifyDataSetChanged();
    }

    public interface TrailersAdapterOnClickHandler {
        void onClick(Trailer trailer);
    }

    public TrailersAdapter(TrailersAdapterOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }

    @NonNull
    @Override
    public TrailersAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailers_content, parent, false);
        return new TrailersAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull TrailersAdapterViewHolder holder, int position) {
        Picasso.with(holder.mPoster.getContext())
                .load("https://img.youtube.com/vi/" + trailers.get(position).getKey() + "/0.jpg")
                .into(holder.mPoster);
    }

    @Override
    public int getItemCount() {
        if (trailers == null) return 0;
        else return trailers.size();
    }

    public class TrailersAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView mPoster;
        public TrailersAdapterViewHolder(View itemView) {
            super(itemView);
            this.mPoster = itemView.findViewById(R.id.detail_youtube_thumbnail_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Trailer trailer = trailers.get(adapterPosition);
            mClickHandler.onClick(trailer);
        }
    }
}
