package com.examearn.popularmovies.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.examearn.popularmovies.R;
import com.examearn.popularmovies.model.Movie;
import com.examearn.popularmovies.model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private static final String BASE_POSTER_PATH = "https://image.tmdb.org/t/p/w185/";

    private List<Movie> mMovieDetailList;
    private OnFavouriteMovieClickListener mOnFavouriteMovieClickListener;

    public MovieAdapter(OnFavouriteMovieClickListener favouriteMovieClickListener) {
        mMovieDetailList = new ArrayList<>();
        this.mOnFavouriteMovieClickListener = favouriteMovieClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_content,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(mMovieDetailList.get(position));
        Picasso.get().load(BASE_POSTER_PATH + mMovieDetailList.get(position).getPosterPath())
                .placeholder(R.drawable.movie_icon).into(holder.imageView);
        holder.itemView.setOnClickListener(v ->
                mOnFavouriteMovieClickListener.onFavouriteMovieClickDetails((Movie) v.getTag()));
    }

    @Override
    public int getItemCount() {
        return mMovieDetailList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.image_view_photo);
        }
    }

    public void setMovieDetailList(List<Movie> detailList) {
        this.mMovieDetailList = detailList;
        notifyDataSetChanged();
    }

    public interface OnFavouriteMovieClickListener {
        void onFavouriteMovieClickDetails(Movie movie);
    }
}
