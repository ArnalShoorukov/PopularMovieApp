package com.example.arnal.popularmoviesapp;

import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.app.LoaderManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arnal.popularmoviesapp.Model.Movies;
import com.example.arnal.popularmoviesapp.Model.MoviesAPI;
import com.example.arnal.popularmoviesapp.Model.Trailer;
import com.example.arnal.popularmoviesapp.Query.QueryTrailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SingleViewActivity extends AppCompatActivity {

    final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    final String POSTER_WIDTH = "w500";
    private Bundle mBundle;
    MoviesAPI api;
    Retrofit retrofit;
    private static final String ENDPOINT = "http://api.themoviedb.org/3/";
    static final String MOVIE_ID = "movieId";
    int movieId;
    private Movies movie;

    private static final int TRAILER_LOADER_ID = 1;
    private static final int SORT_ORDER_POPULAR = 0;
    private static final int SORT_ORDER_TOP_RATED = 1;
    private static String SORT_ORDER;
    final String apiKey = "80e9deb03aed4fda8b09ada4dbfbcac4";
    public String idMovieString = "356305";
    private final String POPULARITY_URL =
            "http://api.themoviedb.org/3/movie/popular?api_key=";
    private final String HIGHEST_RATED_URL =
            "http://api.themoviedb.org/3/movie/top_rated?api_key=";
    //private MovieAdapter mAdapter;

    List<Trailer> trailers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_view);

        Intent intent = getIntent();
        Movies currentMovie = intent.getParcelableExtra("movie");

        ///   mAdapter = new MovieAdapter(this, new ArrayList<Movies>());

        //Find the image view with view ID tv_movie_poster
        ImageView moviePosterView = (ImageView) findViewById(R.id.poster_path);
        String moviePosterPath = new String(currentMovie.getPosterPath());

        // Using picasso, get the poster from the current poster object and set this image on the imageView
        final String FULL_POSTER_URL = POSTER_BASE_URL + POSTER_WIDTH + moviePosterPath;
        Picasso.with(getBaseContext()).load(FULL_POSTER_URL).into(moviePosterView);

        TextView title = (TextView) findViewById(R.id.titleView);
        String titleMovie = new String(currentMovie.getTitle());
        title.setText(titleMovie);


        TextView overview = (TextView) findViewById(R.id.sectionView);
        String overviewMovie = new String(currentMovie.getOverview());
        overview.setText(overviewMovie);
/*
        TextView rating = (TextView) findViewById(R.id.userRating);
        String ratingMovie = new String(currentMovie.getPopularity());
        rating.setText(ratingMovie);*/

        TextView date = (TextView) findViewById(R.id.publishedView);
        String dateMovie = new String(currentMovie.getReleaseDate());
        date.setText(dateMovie);


        retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(MoviesAPI.class);

        api.getMovies(movieId, apiKey).enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                movie = response.body();


            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
               /* Toast toast = Toast.makeText( R.string.no_internet_connection, Toast.LENGTH_LONG);
                toast.show();*/
            }
        });

    }

}


