package com.examearn.popularmovies;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.examearn.popularmovies.adapter.ReviewAdapter;
import com.examearn.popularmovies.adapter.TrailerAdapter;
import com.examearn.popularmovies.model.Movie;
import com.examearn.popularmovies.model.ReviewResult;
import com.examearn.popularmovies.model.Trailer;
import com.examearn.popularmovies.model.TrailerResult;
import com.examearn.popularmovies.network.RetrofitCall;
import com.examearn.popularmovies.room.MovieViewModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailFragment extends Fragment implements TrailerAdapter.OnTrailerClickListener {

    private static final String MOVIE_FAVOURITE_KEY = "fav";
    private static final String MOVIE_DATA_KEY = "movie_data";

    private MovieViewModel mMovieViewModel;
    private Movie movie;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        movie = bundle.getParcelable(MOVIE_DATA_KEY);

        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(movie.getTitle());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);

        String favKey = getArguments().getString(MOVIE_FAVOURITE_KEY);
        mMovieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        FloatingActionButton floatingActionButton = rootView.findViewById(R.id.movie_fav_landscape);
        Executors.newSingleThreadExecutor().execute(() -> {
            if (mMovieViewModel.getMovieId(movie.getId()) != null) {
                floatingActionButton.setImageDrawable(ContextCompat.getDrawable(rootView.getContext(),
                        R.drawable.favorite_icon_selected));
            }
        });

        if (favKey != null && favKey.equals(getString(R.string.favourite_show_value))) {
            floatingActionButton.show();
            floatingActionButton.setOnClickListener(v -> Executors.newSingleThreadExecutor()
                    .execute(() -> {
                        if (mMovieViewModel.getMovieId(movie.getId()) != null) {
                            floatingActionButton.setImageDrawable(ContextCompat.getDrawable(
                                    rootView.getContext(), R.drawable.favorite_icon));
                            mMovieViewModel.delete(movie);
                        } else {
                            floatingActionButton.setImageDrawable(ContextCompat.getDrawable(
                                    rootView.getContext(), R.drawable.favorite_icon_selected));
                            mMovieViewModel.insert(movie);
                        }
                    }));
        }

        TextView movieTitle = rootView.findViewById(R.id.movie_title);
        movieTitle.setText(movie.getTitle());
        TextView movieDescription = rootView.findViewById(R.id.movie_description);
        movieDescription.setText(movie.getOverview());
        RatingBar movieRatingBar = rootView.findViewById(R.id.movie_vote_average);
        movieRatingBar.setRating(Float.parseFloat(movie.getVoteAverage()) / 2.0f);
        Calendar calendar = Calendar.getInstance();
        TextView movieYear = rootView.findViewById(R.id.movie_year);
        try {
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(movie.getYear()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        movieYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));

        final RecyclerView recyclerView = rootView.findViewById(R.id.trailer_recycler);
        RetrofitCall.getInstance().getApiResponse().getMovieTrailers(movie.getId(), BuildConfig.API_KEY)
                .enqueue(new Callback<TrailerResult>() {
                    @Override
                    public void onResponse(Call<TrailerResult> call, Response<TrailerResult> response) {
                        TrailerAdapter adapter = new TrailerAdapter(MovieDetailFragment.this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                                LinearLayoutManager.HORIZONTAL, false));
                        adapter.setTrailerDetailList(response.body().getResults());
                    }

                    @Override
                    public void onFailure(Call<TrailerResult> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

        final RecyclerView reviewRecyclerView = rootView.findViewById(R.id.review_recycler);
        RetrofitCall.getInstance().getApiResponse().getMovieReview(movie.getId(), BuildConfig.API_KEY)
                .enqueue(new Callback<ReviewResult>() {
                    @Override
                    public void onResponse(Call<ReviewResult> call, Response<ReviewResult> response) {
                        ReviewAdapter adapter = new ReviewAdapter();
                        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        reviewRecyclerView.setAdapter(adapter);
                        adapter.setReviewDetails(response.body().getResults());
                    }

                    @Override
                    public void onFailure(Call<ReviewResult> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

        return rootView;
    }

    @Override
    public void onTrailerClickDetail(Trailer trailer) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(getResources().getString(R.string.youtube_watch_path)
                        + trailer.getKey()));
        startActivity(intent);
    }

}
