package com.antarikshc.theguardiannews;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<NewsData>> {

    /**
     * global declarations
     **/
    Intent searchIntent;
    String SEARCH_NEWS_URL;

    int scrollState;

    Toolbar toolbar;
    ListView searchNewsList;
    private CustomAdapter searchNewsAdapter;
    private TextView EmptyStateTextView;
    ProgressBar loadSpin;

    LoaderManager loaderManager;

    //we are using different loaders for each tab
    private static int SEARCH_NEWS_LOADER = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = findViewById(R.id.search_activity_toolbar);
        searchNewsList = findViewById(R.id.search_news_list);

        //user interactive views
        loadSpin = findViewById(R.id.loadSpin);
        EmptyStateTextView = findViewById(R.id.emptyView);

        searchIntent = getIntent();

        //set the title as the search query on toolbar
        String title = searchIntent.getStringExtra("title");
        title = title.substring(0, 1).toUpperCase() + title.substring(1);
        toolbar.setTitle(title);

        //set it as action bar to retrieve it back
        setSupportActionBar(toolbar);

        //Add back button in Toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SEARCH_NEWS_URL = searchIntent.getStringExtra("url");

        loadSpin.setVisibility(View.VISIBLE);
        searchNewsList.animate().alpha(0.1f).setDuration(400);

        //initiate CustomAdapter and set it for worldNewsList
        searchNewsAdapter = new CustomAdapter(getApplicationContext(), new ArrayList<NewsData>());
        searchNewsList.setAdapter(searchNewsAdapter);

        //set empty text view for a proper msg to user
        searchNewsList.setEmptyView(EmptyStateTextView);

        loaderManager = getSupportLoaderManager();

        if (checkNet() && searchNewsAdapter.getCount() == 0) {
            executeLoader();
        } else {
            EmptyStateTextView.setText(R.string.no_network);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //This is to prevent listview losing the scroll position
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            outState.putInt("CURRENT_SCROLL", searchNewsList.getFirstVisiblePosition());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            scrollState = savedInstanceState.getInt("CURRENT_SCROLL");
            searchNewsList.setSelection(scrollState);
        }
    }

    //Initiate and destroy loader methods to be called after search is submitted
    private void executeLoader() {
        loaderManager.initLoader(SEARCH_NEWS_LOADER, null, this);
    }

    @NonNull
    @Override
    public Loader<ArrayList<NewsData>> onCreateLoader(int id, @Nullable Bundle args) {
        return new NewsLoader(this, SEARCH_NEWS_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<NewsData>> loader, ArrayList<NewsData> news) {
        EmptyStateTextView.setText(R.string.no_news);

        // Clear the adapter of previous books data
        searchNewsAdapter.clear();

        loadSpin.setVisibility(View.GONE);

        if (news != null && !news.isEmpty()) {
            searchNewsAdapter.addAll(news);
            searchNewsList.animate().alpha(1.0f).setDuration(400);
            searchNewsList.setSelection(scrollState);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<NewsData>> loader) {
        searchNewsAdapter.clear();
    }

    //Check internet is connected or not, to notify user
    public boolean checkNet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}