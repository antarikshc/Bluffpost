package com.antarikshc.theguardiannews;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class SportsFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<NewsData>> {

    /**
     * API KEY
     **/
    private static String API_KEY = "753d66b9-55a1-4196-bc18-57c05d86c5ce";

    //Books loaded ID, default = 1 currently using single Loader
    private static int SPORTS_NEWS_LOADER = 20;

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

        sportsNewsList = view.findViewById(R.id.sports_news_list);

        //user interactive views
        loadSpin = view.findViewById(R.id.loadSpin);
        EmptyStateTextView = view.findViewById(R.id.emptyView);

        sportsNewsList.animate().alpha(0.1f).setDuration(400);

        //initiate CustomAdapter and set it for worldNewsList
        sportsNewsAdapter = new CustomAdapter(getContext(), new ArrayList<NewsData>());
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
        sportsUri.appendQueryParameter("show-fields", "thumbnail,headline");
        sportsUri.appendQueryParameter("api-key", API_KEY);

        loaderManager = getActivity().getSupportLoaderManager();

        //this is to prevent loader from re-fetching the data
        if (sportsNewsAdapter.getCount() == 0) {
            executeLoader();
        }

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