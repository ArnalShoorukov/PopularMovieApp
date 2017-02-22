package com.example.arnal.popularmoviesapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by arnal on 2/16/17.
 */

public class MovieLoader extends AsyncTaskLoader<List<Movies>> {

    /** Tag for log messages */
    private static final String LOG_TAG = MovieLoader.class.getName();

    /** Query URL */
    private String mUrl;

    public MovieLoader(Context context, String url) {
        super(context);
        mUrl =url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.v("Loader State","Loader starts loading");
        super.onStartLoading();
    }

    @Override
    public List<Movies> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<Movies> movies = QueryUtils.fetchMovieData(mUrl);
        return movies;
    }
}
