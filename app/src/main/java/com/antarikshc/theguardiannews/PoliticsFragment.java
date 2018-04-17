package com.antarikshc.theguardiannews;

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

import java.util.ArrayList;

public class PoliticsFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<NewsData>> {

    /**
     * API KEY
     **/
    private static String API_KEY = "753d66b9-55a1-4196-bc18-57c05d86c5ce";

    //Books loaded ID, default = 1 currently using single Loader
    private static int POLITICS_NEWS_LOADER = 25;

    /**
     * global declarations
     **/
    View view;

    int scrollState;

    ListView politicsNewsList;
    private CustomAdapter politicsNewsAdapter;
    private TextView EmptyStateTextView;
    ProgressBar loadSpin;
    Uri.Builder politicsUri;
    SwipeRefreshLayout swipeRefreshLayout;

    LoaderManager loaderManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.politics_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout = view.findViewById(R.id.politics_refresh);
        politicsNewsList = view.findViewById(R.id.politics_news_list);

        //user interactive views
        loadSpin = view.findViewById(R.id.loadSpin);
        EmptyStateTextView = view.findViewById(R.id.emptyView);

        politicsNewsList.animate().alpha(0.1f).setDuration(400);

        //initiate CustomAdapter and set it for worldNewsList
        politicsNewsAdapter = new CustomAdapter(getActivity().getApplicationContext(), new ArrayList<NewsData>());
        politicsNewsList.setAdapter(politicsNewsAdapter);

        //set empty text view for a proper msg to user
        politicsNewsList.setEmptyView(EmptyStateTextView);

        /** URL to fetch data for Politics News**/
        Uri baseUri = Uri.parse("https://content.guardianapis.com/search");
        politicsUri = baseUri.buildUpon();

        politicsUri.appendQueryParameter("q", "politics");
        politicsUri.appendQueryParameter("format", "json");
        politicsUri.appendQueryParameter("page-size", "8");
        politicsUri.appendQueryParameter("from-date", "2018-01-01");
        politicsUri.appendQueryParameter("show-fields", "thumbnail,headline");
        politicsUri.appendQueryParameter("api-key", API_KEY);

        loaderManager = getActivity().getSupportLoaderManager();

        //this is to prevent loader from re-fetching the data
        if (politicsNewsAdapter.getCount() == 0) {
            executeLoader();
        }

        politicsNewsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsData currentData = politicsNewsAdapter.getItem(position);

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

                POLITICS_NEWS_LOADER += 1;
                swipeRefreshLayout.setRefreshing(true);
                executeLoader();

                destroyLoader(POLITICS_NEWS_LOADER - 1);

                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    //This is to prevent listview losing the scroll position
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            outState.putInt("CURRENT_SCROLL", politicsNewsList.getFirstVisiblePosition());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            scrollState = savedInstanceState.getInt("CURRENT_SCROLL");
            politicsNewsList.setSelection(scrollState);
        }
    }

    //Initiate and destroy loader methods to be called after search is submitted
    private void executeLoader() {
        loaderManager.initLoader(POLITICS_NEWS_LOADER, null, this);
    }

    private void destroyLoader(int id) {
        loaderManager.destroyLoader(id);
    }

    @NonNull
    @Override
    public Loader<ArrayList<NewsData>> onCreateLoader(int id, @Nullable Bundle args) {
        return new NewsLoader(getActivity(), politicsUri.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<NewsData>> loader, ArrayList<NewsData> news) {
        EmptyStateTextView.setText(R.string.no_news);

        // Clear the adapter of previous books data
        politicsNewsAdapter.clear();

        loadSpin.setVisibility(View.GONE);

        if (news != null && !news.isEmpty()) {
            politicsNewsAdapter.addAll(news);
            politicsNewsList.animate().alpha(1.0f).setDuration(400);
            politicsNewsList.setSelection(scrollState);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<NewsData>> loader) {
        politicsNewsAdapter.clear();
    }
}