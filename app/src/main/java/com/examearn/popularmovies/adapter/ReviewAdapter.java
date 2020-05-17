package com.examearn.popularmovies.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.examearn.popularmovies.R;
import com.examearn.popularmovies.model.Review;

import java.util.List;

public class ReviewAdapter
        extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<Review> reviews;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.movieContent.setText(reviews.get(position).getContent());
        holder.movieAuthor.setText(reviews.get(position).getAuthor());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void setReviewDetails(List<Review> reviewDetails) {
        this.reviews = reviewDetails;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView movieContent;
        TextView movieAuthor;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            movieContent = itemView.findViewById(R.id.movie_review_text);
            movieAuthor = itemView.findViewById(R.id.movie_review_author);
        }
    }
}

