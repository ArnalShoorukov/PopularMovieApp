package com.example.arnal.popularmoviesapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movies>>{

    private static final String LOG_TAG = MainActivity.class.getName();
    /**
     * +     * Constant value for the movie loader ID. We can choose any integer.
     * +     * This really only comes into play if you're using multiple loaders.
     */
    private static final int MOVIE_LOADER_ID = 1;
    private static final int SORT_ORDER_POPULAR = 0;
    private static final int SORT_ORDER_TOP_RATED = 1;
    private static String SORT_ORDER;
    final String apiKey = "My API";
    private final String POPULARITY_URL =
            "http://api.themoviedb.org/3/movie/popular?api_key=";
    private final String HIGHEST_RATED_URL =
            "http://api.themoviedb.org/3/movie/top_rated?api_key=";
    ArrayList<Movies> mMovieAdapter;
    SharedPreferences sharedPrefs;
    /*ListView global variable*/
    private GridView movieListView;
    //Adapter for list of books
    private MovieAdapter mAdapter;
    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (id == R.id.action_popular) {
            sharedPrefs.edit().putInt(SORT_ORDER, SORT_ORDER_POPULAR).apply();
            queryAPI();
        }
        if (id == R.id.action_top_rated) {
            sharedPrefs.edit().putInt(SORT_ORDER, SORT_ORDER_TOP_RATED).apply();
            queryAPI();
        }

        return super.onOptionsItemSelected(item);
    }

    private void queryAPI() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int sortOrder = sharedPreferences.getInt(SORT_ORDER, SORT_ORDER_POPULAR);
        switch (sortOrder) {

            case SORT_ORDER_POPULAR:
                SORT_ORDER = "popular";
                //query popular URL
                Uri.parse(POPULARITY_URL);
                Log.i(LOG_TAG, "Show me URL for popular: " + POPULARITY_URL);
                getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
                mAdapter.notifyDataSetChanged();
                break;

            case SORT_ORDER_TOP_RATED:
                SORT_ORDER = "top_rated";
                //query top rated URL
                Uri.parse(HIGHEST_RATED_URL);
                Log.i(LOG_TAG, "Show me URL for top rated: " + HIGHEST_RATED_URL);
                getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set default preferences when the app starts (order by relevance)
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        PreferenceManager.setDefaultValues(this, R.xml.settings_main, true);

        SORT_ORDER = sharedPrefs.getString(
                getString(R.string.settings_sort_by_default),
                getString(R.string.main_menu_action_popular));
        mMovieAdapter = new ArrayList<Movies>();

        movieListView = (GridView) findViewById(R.id.list);
        // Create a new {@link ArrayAdapter} of news
        mAdapter = new MovieAdapter(this, new ArrayList<Movies>());
        movieListView.setAdapter(mAdapter);
        mEmptyStateTextView = (TextView) findViewById(R.id.text);

        movieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Start the details activity
                Movies dataToSend = mAdapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, SingleViewActivity.class);
                intent.putExtra("movie", dataToSend);
                startActivity(intent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            android.app.LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(MOVIE_LOADER_ID, null, this);
            //check if there's already a loader
            if (getLoaderManager().getLoader(MOVIE_LOADER_ID).isStarted()) {
                //restart it if there's one
                getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
            }
        } else {
            //Otherwise,display error
            //First hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading);
            loadingIndicator.setVisibility(View.GONE);
            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        mAdapter.clear();
        movieListView.setAdapter(mAdapter);
        movieListView.setEmptyView(mEmptyStateTextView);
    }

    @Override
    public Loader<List<Movies>> onCreateLoader(int id, Bundle args) {

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http");
        builder.authority("api.themoviedb.org");
        builder.appendEncodedPath("3/movie/" + SORT_ORDER);
        builder.appendQueryParameter("api_key", apiKey);
        Log.v("TEST", "TEST" + builder.toString());

        return new MovieLoader(this, builder.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();

        PreferenceManager.setDefaultValues(this, R.xml.settings_main, true);
        getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
    }

    @Override
    public void onLoadFinished(Loader<List<Movies>> loader, List<Movies> data) {

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No books found."
        mEmptyStateTextView.setText(R.string.no_news);

        // Clear the adapter of previous book data
        mAdapter.clear();
        // If there is a valid list of {@link News}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
            mAdapter.notifyDataSetChanged();
        }
        Log.v("Loader State","on Load Finished");
    }

    @Override
    public void onLoaderReset(Loader<List<Movies>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}
