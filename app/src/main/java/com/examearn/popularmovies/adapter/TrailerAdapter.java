package com.examearn.popularmovies.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.examearn.popularmovies.R;
import com.examearn.popularmovies.model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrailerAdapter
        extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private static final String thumbnailPath = "https://img.youtube.com/vi/";
    private static final String thumbnailExtension = "/0.jpg";
    private List<Trailer> mTrailers;
    private OnTrailerClickListener mOnTrailerClickListener;
    private static Trailer firstTrailerDetail;

    public TrailerAdapter(OnTrailerClickListener onTrailerClickListener) {
        this.mOnTrailerClickListener = onTrailerClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_thumnail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.itemView.setTag(mTrailers.get(position));
        Picasso.get().load((thumbnailPath + mTrailers.get(position).getKey() + thumbnailExtension))
                .placeholder(R.drawable.movie_icon).into(holder.imageView);
        holder.itemView.setOnClickListener(v ->
                mOnTrailerClickListener.onTrailerClickDetail((Trailer) v.getTag()));
        firstTrailerDetail = mTrailers.get(position);
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.trailer_image_view_photo);
        }
    }

    public void setTrailerDetailList(List<Trailer> detailList) {
        this.mTrailers = detailList;
    }

    public interface OnTrailerClickListener {
        void onTrailerClickDetail(Trailer trailer);
    }

    public static Trailer getFirstTrailerDetail() {
        return firstTrailerDetail;
    }

}
