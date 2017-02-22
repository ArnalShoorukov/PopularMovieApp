package com.example.arnal.popularmoviesapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by arnal on 2/16/17.
 */

public class MovieAdapter extends ArrayAdapter<Movies> {


    public MovieAdapter(Activity context, ArrayList<Movies> movies) {
        super(context, 0, movies);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listView = convertView;
        if(listView == null){
            listView = LayoutInflater.from(getContext()).inflate(R.layout.list_movies, parent, false);
        }
        Movies currentMovie = getItem(position);
        ImageView poster =(ImageView)listView.findViewById(R.id.poster_path);
        String moviePosterPath = currentMovie.getPosterPath();
        final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
        final String POSTER_WIDTH = "w185";
        final String FULL_POSTER_URL = POSTER_BASE_URL + POSTER_WIDTH + moviePosterPath;
        Picasso.with(getContext()).load(FULL_POSTER_URL).into(poster);

        return listView;
    }
}