package com.antarikshc.theguardiannews.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.antarikshc.theguardiannews.R;
import com.antarikshc.theguardiannews.datasource.NewsLoader;
import com.antarikshc.theguardiannews.model.NewsData;
import com.antarikshc.theguardiannews.util.Master;

import java.util.ArrayList;

public class SportsFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<NewsData>> {

    /**
     * API KEY
     **/
    private static String API_KEY = "753d66b9-55a1-4196-bc18-57c05d86c5ce";

    //Books loaded ID, default = 1 currently using single Loader
    private static int SPORTS_NEWS_LOADER = 50;

    /**
     * global declarations
     **/
    View view;

    int scrollState;

    ListView sportsNewsList;
    private CustomAdapter sportsNewsAdapter;
    private TextView EmptyStateTextView;
    ProgressBar loadSpin;
    Uri.Builder sportsUri;
    SwipeRefreshLayout swipeRefreshLayout;

    LoaderManager loaderManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.sports_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout = view.findViewById(R.id.sports_refresh);
        sportsNewsList = view.findViewById(R.id.sports_news_list);

        //user interactive views
        loadSpin = view.findViewById(R.id.load_spin);
        EmptyStateTextView = view.findViewById(R.id.empty_view);

        sportsNewsList.animate().alpha(0.1f).setDuration(400);

        //initiate CustomAdapter and set it for worldNewsList
        sportsNewsAdapter = new CustomAdapter(getActivity().getApplicationContext(), new ArrayList<NewsData>());
        sportsNewsList.setAdapter(sportsNewsAdapter);

        //set empty text view for a proper msg to user
        sportsNewsList.setEmptyView(EmptyStateTextView);

        /** URL to fetch data for Sports News**/
        Uri baseUri = Uri.parse("https://content.guardianapis.com/search");
        sportsUri = baseUri.buildUpon();

        sportsUri.appendQueryParameter("q", "sports");
        sportsUri.appendQueryParameter("format", "json");
        sportsUri.appendQueryParameter("page-size", "8");
        sportsUri.appendQueryParameter("from-date", "2018-01-01");
        sportsUri.appendQueryParameter("show-fields", "thumbnail,headline,byline");
        sportsUri.appendQueryParameter("show-tags", "contributor");
        sportsUri.appendQueryParameter("api-key", API_KEY);

        loaderManager = getActivity().getSupportLoaderManager();

        //this is to prevent loader from re-fetching the data
        if (sportsNewsAdapter.getCount() == 0 && Master.checkNet(getContext())) {
            executeLoader();
        } else {
            loadSpin.setVisibility(View.GONE);
            EmptyStateTextView.setText(R.string.no_network);
        }

        sportsNewsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsData currentData = sportsNewsAdapter.getItem(position);

                if (currentData != null) {

                    try {

                        //Explicit Intent
                        Intent webActivityIntent = new Intent(getContext(), WebviewActivity.class);
                        webActivityIntent.putExtra("url", currentData.getWebUrl());
                        startActivity(webActivityIntent);

                    } catch (Exception e) {

                        //Implicit Intent
                        Uri webUri = Uri.parse(currentData.getWebUrl());
                        Intent webBrowserIntent = new Intent(Intent.ACTION_VIEW, webUri);

                        if (webBrowserIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivity(webBrowserIntent);
                        }
                    }
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                EmptyStateTextView.setText("");

                SPORTS_NEWS_LOADER += 1;
                swipeRefreshLayout.setRefreshing(true);

                if (Master.checkNet(getContext())) {
                    executeLoader();
                } else {
                    loadSpin.setVisibility(View.GONE);
                    EmptyStateTextView.setText(R.string.no_network);
                }

                destroyLoader(SPORTS_NEWS_LOADER - 1);

                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    //This is to prevent listview losing the scroll position
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            outState.putInt("CURRENT_SCROLL", sportsNewsList.getFirstVisiblePosition());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            scrollState = savedInstanceState.getInt("CURRENT_SCROLL");
            sportsNewsList.setSelection(scrollState);
        }
    }

    //Initiate and destroy loader methods to be called after search is submitted
    private void executeLoader() {
        loaderManager.initLoader(SPORTS_NEWS_LOADER, null, this);
    }

    private void destroyLoader(int id) {
        loaderManager.destroyLoader(id);
    }

    @NonNull
    @Override
    public Loader<ArrayList<NewsData>> onCreateLoader(int id, @Nullable Bundle args) {
        return new NewsLoader(getActivity(), sportsUri.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<NewsData>> loader, ArrayList<NewsData> news) {
        EmptyStateTextView.setText(R.string.no_news);

        // Clear the adapter of previous books data
        sportsNewsAdapter.clear();

        loadSpin.setVisibility(View.GONE);

        if (news != null && !news.isEmpty()) {
            sportsNewsAdapter.addAll(news);
            sportsNewsList.animate().alpha(1.0f).setDuration(400);
            sportsNewsList.setSelection(scrollState);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<NewsData>> loader) {
        sportsNewsAdapter.clear();
    }
}