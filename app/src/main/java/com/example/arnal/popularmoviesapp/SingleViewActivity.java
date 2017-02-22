package com.example.arnal.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class SingleViewActivity extends AppCompatActivity {

    final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    final String POSTER_WIDTH = "w500";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_view);

        Intent intent = getIntent();
        Movies currentMovie = intent.getParcelableExtra("movie");

        //Find the image view with view ID tv_movie_poster
        ImageView moviePosterView = (ImageView) findViewById(R.id.poster_path);
        String moviePosterPath = new String (currentMovie.getPosterPath());

        // Using picasso, get the poster from the current poster object and set this image on the imageView
        final String FULL_POSTER_URL = POSTER_BASE_URL + POSTER_WIDTH + moviePosterPath;
        Picasso.with(getBaseContext()).load(FULL_POSTER_URL).into(moviePosterView);

        TextView title = (TextView) findViewById(R.id.titleView);
        String titleMovie = new String(currentMovie.getTitle());
        title.setText(titleMovie);

        TextView overview = (TextView) findViewById(R.id.sectionView);
        String overviewMovie = new String(currentMovie.getOverview());
        overview.setText(overviewMovie);

        TextView rating = (TextView) findViewById(R.id.userRating);
        String ratingMovie = new String(currentMovie.getRating());
        rating.setText(ratingMovie);

        TextView date = (TextView) findViewById(R.id.publishedView);
        String dateMovie = new String(currentMovie.getReleaseDate());
        date.setText(dateMovie);
    }
}


