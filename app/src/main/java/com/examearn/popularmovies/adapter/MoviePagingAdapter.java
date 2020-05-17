package com.examearn.popularmovies.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.examearn.popularmovies.R;
import com.examearn.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

public class MoviePagingAdapter extends PagedListAdapter<Movie, MoviePagingAdapter.ViewHolder> {

    private static final String BASE_POSTER_PATH = "https://image.tmdb.org/t/p/w185/";
    private static DiffUtil.ItemCallback<Movie> DIFF_CALLBACK = new DiffUtil.ItemCallback<Movie>() {
        @Override
        public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return true;
        }
    };
    private OnMovieClickListener mOnMovieClickListener;

    public MoviePagingAdapter(OnMovieClickListener movieClickListener) {
        super(DIFF_CALLBACK);
        Log.d("constructorCalled", " constructor");
        this.mOnMovieClickListener = movieClickListener;
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
        Picasso.get().load(BASE_POSTER_PATH + getItem(position).getPosterPath())
                .placeholder(R.drawable.movie_icon).into(holder.imageView);
        holder.itemView.setOnClickListener(v ->
                mOnMovieClickListener.onMovieClickDetails(getItem(position)));
    }

    public interface OnMovieClickListener {
        void onMovieClickDetails(Movie movie);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.image_view_photo);
        }
    }
}
