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

public class WorldFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<NewsData>> {

    // we are using different loaders for each tab
    private static int WORLD_NEWS_LOADER = 1;

    // Global Params
    private View view;

    private int scrollState;
    private ListView worldNewsList;
    private CustomAdapter worldNewsAdapter;
    private TextView EmptyStateTextView;
    private ProgressBar loadSpin;
    private Uri.Builder worldUri;
    private SwipeRefreshLayout swipeRefreshLayout;

    private LoaderManager loaderManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        //inflate the fragment with world_fragment layout
        view = inflater.inflate(R.layout.world_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        worldNewsList = view.findViewById(R.id.world_news_list);

        //user interactive views
        loadSpin = view.findViewById(R.id.load_spin);
        EmptyStateTextView = view.findViewById(R.id.empty_view);

        swipeRefreshLayout = view.findViewById(R.id.world_refresh);
        worldNewsList.animate().alpha(0.1f).setDuration(400);

        //initiate CustomAdapter and set it for worldNewsList
        worldNewsAdapter = new CustomAdapter(getActivity().getApplicationContext(), new ArrayList<NewsData>());
        worldNewsList.setAdapter(worldNewsAdapter);

        //set empty text view for a proper msg to user
        worldNewsList.setEmptyView(EmptyStateTextView);

        // URL to fetch data for World news
        worldUri = Master.getWorldUri();

        worldUri.appendQueryParameter("show-editors-picks", "true");
        worldUri.appendQueryParameter("format", "json");
        worldUri.appendQueryParameter("from-date", "2017-03-01");
        worldUri.appendQueryParameter("show-fields", "thumbnail,headline,byline");
        worldUri.appendQueryParameter("show-tags", "contributor");
        worldUri.appendQueryParameter("api-key", Master.getAPIKey());

        loaderManager = getActivity().getSupportLoaderManager();

        //this is to prevent loader from re-fetching the data
        if (worldNewsAdapter.getCount() == 0 && Master.checkNet(getContext())) {
            executeLoader();
        } else {
            loadSpin.setVisibility(View.GONE);
            EmptyStateTextView.setText(R.string.no_network);
        }

        worldNewsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsData currentData = worldNewsAdapter.getItem(position);

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

                WORLD_NEWS_LOADER += 1;
                swipeRefreshLayout.setRefreshing(true);

                if (Master.checkNet(getContext())) {
                    executeLoader();
                } else {
                    loadSpin.setVisibility(View.GONE);
                    EmptyStateTextView.setText(R.string.no_network);
                }

                destroyLoader(WORLD_NEWS_LOADER - 1);

                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    //This is to prevent listview losing the scroll position
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            outState.putInt("CURRENT_SCROLL", worldNewsList.getFirstVisiblePosition());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            scrollState = savedInstanceState.getInt("CURRENT_SCROLL");
            worldNewsList.setSelection(scrollState);
        }
    }

    //Initiate and destroy loader methods to be called after search is submitted
    private void executeLoader() {
        loaderManager.initLoader(WORLD_NEWS_LOADER, null, this);
    }

    private void destroyLoader(int id) {
        loaderManager.destroyLoader(id);
    }

    @NonNull
    @Override
    public Loader<ArrayList<NewsData>> onCreateLoader(int id, @Nullable Bundle args) {
        return new NewsLoader(getActivity(), worldUri.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<NewsData>> loader, ArrayList<NewsData> news) {
        EmptyStateTextView.setText(R.string.no_news);

        // Clear the adapter of previous books data
        worldNewsAdapter.clear();

        loadSpin.setVisibility(View.GONE);

        if (news != null && !news.isEmpty()) {
            worldNewsAdapter.addAll(news);
            worldNewsList.animate().alpha(1.0f).setDuration(400);
            worldNewsList.setSelection(scrollState);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<NewsData>> loader) {
        worldNewsAdapter.clear();
    }

}