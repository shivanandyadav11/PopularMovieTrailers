package com.examearn.popularmovies;

import android.content.Intent;
import android.os.Bundle;

import com.examearn.popularmovies.adapter.TrailerAdapter;
import com.examearn.popularmovies.model.Movie;
import com.examearn.popularmovies.room.MovieViewModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.concurrent.Executors;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String MOVIE_DATA_KEY = "movie_data";
    private MovieViewModel mMovieViewModel;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ImageView mPosterImage = findViewById(R.id.movie_poster_image);

        mMovieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        movie = getIntent().getParcelableExtra(MOVIE_DATA_KEY);

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        Executors.newSingleThreadExecutor().execute(() -> {
            if (mMovieViewModel.getMovieId(movie.getId()) != null) {
                floatingActionButton.setImageDrawable(ContextCompat.getDrawable(this,
                        R.drawable.favorite_icon_selected));
            }
        });

        floatingActionButton.setOnClickListener(view ->
                Executors.newFixedThreadPool(4).execute(() -> {
                    if (mMovieViewModel.getMovieId(movie.getId()) != null) {
                        mMovieViewModel.delete(movie);
                        floatingActionButton.setImageDrawable(ContextCompat.getDrawable(this,
                                R.drawable.favorite_icon));
                    } else {
                        mMovieViewModel.insert(movie);
                        floatingActionButton.setImageDrawable(ContextCompat.getDrawable(this,
                                R.drawable.favorite_icon_selected));
                    }
                }));

        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Picasso.get().load(getResources().getString(R.string.movie_poster_path) + movie.getPosterPath()).into(mPosterImage);
        CollapsingToolbarLayout appBarLayout = findViewById(R.id.toolbar_layout);
        appBarLayout.setTitle(movie.getTitle());

        if (savedInstanceState == null) {
            MovieDetailFragment fragment = new MovieDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(MOVIE_DATA_KEY, movie);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_item_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_menu, menu);
        menu.findItem(R.id.popular_movie_menu).setVisible(false);
        menu.findItem(R.id.top_rated_movie_menu).setVisible(false);
        menu.findItem(R.id.favourite_movie_menu).setVisible(false);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
            case R.id.share_movie_menu: {
                shareTextUrl();
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

}
