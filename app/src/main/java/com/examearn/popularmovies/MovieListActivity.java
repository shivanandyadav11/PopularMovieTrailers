package com.examearn.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.examearn.popularmovies.adapter.MoviePagingAdapter;
import com.examearn.popularmovies.adapter.TrailerAdapter;
import com.examearn.popularmovies.model.Movie;
import com.examearn.popularmovies.adapter.MovieAdapter;
import com.examearn.popularmovies.room.MovieViewModel;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MovieListActivity extends AppCompatActivity implements
        MoviePagingAdapter.OnMovieClickListener, MovieAdapter.OnFavouriteMovieClickListener {


    private static final String MOVIE_DATA_KEY = "movie_data";
    private static String movieCategory = "popular";
    private static String MOVIE_CATEGORY_KEY = "movieCategoryKey";

    private RecyclerView recyclerView;
    private MoviePagingAdapter moviePagingAdapter;
    private MovieAdapter favouriteMovieAdapter;
    private MovieViewModel movieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        toolbar.setOverflowIcon(getDrawable(R.drawable.sort_icon));
        recyclerView = findViewById(R.id.recycler_item_list);

        movieViewModel = ViewModelProviders.of(this)
                .get(MovieViewModel.class);

        favouriteMovieAdapter = new MovieAdapter(this);
        moviePagingAdapter = new MoviePagingAdapter(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        recyclerView.setAdapter(moviePagingAdapter);
        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIE_CATEGORY_KEY)) {
            if (savedInstanceState.getString(MOVIE_CATEGORY_KEY)
                    .equals(getResources().getString(R.string.movie_category_favourite))) {
                recyclerView.setAdapter(favouriteMovieAdapter);
            }
        }

        movieViewModel.getInitialMoviePageList().observe(this, movies ->
                moviePagingAdapter.submitList(movies));
        movieViewModel.getListFavouriteData().observe(this,
                favouriteMovieAdapter::setMovieDetailList);

    }

    private void loadMovie() {
        movieViewModel.getMoviePageList(movieCategory).observe(this, movies ->
                moviePagingAdapter.submitList(movies));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_menu, menu);
        if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            menu.findItem(R.id.share_movie_menu).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular_movie_menu: {
                movieCategory = getResources().getString(R.string.movie_category_popular);
                recyclerView.setAdapter(moviePagingAdapter);
                loadMovie();
                break;
            }
            case R.id.top_rated_movie_menu: {
                recyclerView.setAdapter(moviePagingAdapter);
                movieCategory = getResources().getString(R.string.movie_category_top_rated);
                loadMovie();
                break;
            }
            case R.id.favourite_movie_menu: {
                movieCategory = getResources().getString(R.string.movie_category_favourite);
                recyclerView.setAdapter(favouriteMovieAdapter);
                movieViewModel.getListFavouriteData().observe(this,
                        favouriteMovieAdapter::setMovieDetailList);
                break;
            }
            case R.id.share_movie_menu: {
                shareTextUrl();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareTextUrl() {
        if (TrailerAdapter.getFirstTrailerDetail().getKey() != null) {
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType(getResources().getString(R.string.share_text_type));
            share.putExtra(Intent.EXTRA_SUBJECT, TrailerAdapter.getFirstTrailerDetail().getKey());
            share.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_youtube_url)
                    + TrailerAdapter.getFirstTrailerDetail().getKey());
            startActivity(Intent.createChooser(share, getResources().getString(R.string.share_link)));
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_trailer),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMovieClickDetails(Movie movie) {

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(MOVIE_DATA_KEY, movie);
            bundle.putString(getResources().getString(R.string.FAVOURITE_KEY),
                    getResources().getString(R.string.FAVOURITE_VALUE));
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_item_detail_container, fragment)
                    .commit();

        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(MOVIE_DATA_KEY, movie);
            startActivity(intent);
        }
    }

    @Override
    public void onFavouriteMovieClickDetails(Movie movie) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(MOVIE_DATA_KEY, movie);
            bundle.putString("fav", "show");
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_item_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(MOVIE_DATA_KEY, movie);
            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(MOVIE_CATEGORY_KEY, movieCategory);
    }
}
