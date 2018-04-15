package com.antarikshc.theguardiannews;

import android.animation.LayoutTransition;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<NewsData>> {

    public static final String LOG_TAG = MainActivity.class.getName();

    /**
     * URL to fetch data
     **/
    private static Uri baseUri = null;
    Uri.Builder uriBuilder;

    /**
     * API KEY
     **/
    private static String API_KEY = "753d66b9-55a1-4196-bc18-57c05d86c5ce";

    //Books loaded ID, default = 1 currently using single Loader
    private static int NEWS_LOADER_ID = 1;

    /**
     * global declarations
     **/
    RelativeLayout rootLayout;
    ListView newsListView;
    private CustomAdapter customAdapter;
    LoaderManager loaderManager;
    SearchView searchView;
    LinearLayout searchBar;
    MenuItem item;
    private TextView EmptyStateTextView;
    ProgressBar loadSpin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootLayout = findViewById(R.id.root_layout);
        newsListView = findViewById(R.id.newsList);

        newsListView.animate().alpha(0.1f).setDuration(400);

        //initiate CustomAdapter and set it for newsListView
        customAdapter = new CustomAdapter(getApplicationContext(), new ArrayList<NewsData>());
        newsListView.setAdapter(customAdapter);

        //user interactive views
        loadSpin = findViewById(R.id.loadSpin);
        EmptyStateTextView = findViewById(R.id.emptyView);

        baseUri = Uri.parse("https://content.guardianapis.com/search");
        uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("tags", "international");
        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("page-size", "8");
        uriBuilder.appendQueryParameter("from-date", "2017-03-01");
        uriBuilder.appendQueryParameter("show-fields", "thumbnail,headline");
        uriBuilder.appendQueryParameter("order-by", "newest");
        uriBuilder.appendQueryParameter("api-key", API_KEY);

        //set empty text view for a proper msg to user
        newsListView.setEmptyView(EmptyStateTextView);

        loaderManager = getLoaderManager();

        boolean netConnection = checkNet();
        if (netConnection) {
            //we need to call Loader in onCreate
            //else it won't persist through orientations
            executeLoader();
        } else {
            EmptyStateTextView.setText(R.string.no_network);
            loadSpin.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        item = menu.findItem(R.id.app_bar_search);
        searchView = (SearchView) item.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);

        int searchBarId = searchView.getContext().getResources().getIdentifier("android:id/search_bar", null, null);
        searchBar = searchView.findViewById(searchBarId);
        searchBar.setLayoutTransition(new LayoutTransition());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                item.collapseActionView();
                newsListView.animate().alpha(0.1f).setDuration(400);

                //Clear focus so that search bar can be translated back up top
                searchView.setQuery("", false);
                searchView.clearFocus();
                searchBar.clearFocus();

                //remove the Empty State msg to make room for spinner
                EmptyStateTextView.setText("");

                //this will spin till View.GONE is called at onLoadFinished
                loadSpin.setVisibility(View.VISIBLE);

                //build Uri
                baseUri = Uri.parse("https://content.guardianapis.com/search");
                uriBuilder = baseUri.buildUpon();

                uriBuilder.appendQueryParameter("q", query);
                uriBuilder.appendQueryParameter("format", "json");
                uriBuilder.appendQueryParameter("page-size", "8");
                uriBuilder.appendQueryParameter("from-date", "2016-01-01");
                uriBuilder.appendQueryParameter("show-fields", "thumbnail,headline");
                uriBuilder.appendQueryParameter("api-key", API_KEY);

                //destroy previous loader and increment the loader id
                destroyLoader(NEWS_LOADER_ID);
                NEWS_LOADER_ID += 1;
                executeLoader();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (searchView != null) {
            //we don't want search bar in focus when user returns to Main screen
            searchView.setQuery("", false);
            searchView.clearFocus();
            searchBar.clearFocus();
        }
        rootLayout.requestFocus();

        newsListView.setVisibility(View.VISIBLE);
    }

    //Initiate and destroy loader methods to be called after search is submitted
    private void executeLoader() {
        loaderManager.initLoader(NEWS_LOADER_ID, null, this);
    }

    private void destroyLoader(int id) {
        loaderManager.destroyLoader(id);
    }

    @Override
    public Loader<ArrayList<NewsData>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<NewsData>> loader, ArrayList<NewsData> news) {
        EmptyStateTextView.setText(R.string.no_news);

        // Clear the adapter of previous books data
        customAdapter.clear();

        loadSpin.setVisibility(View.GONE);

        if (news != null && !news.isEmpty()) {
            customAdapter.addAll(news);
            newsListView.animate().alpha(1.0f).setDuration(400);

        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<NewsData>> loader) {
        // Loader reset, so we can clear out our existing data.
        customAdapter.clear();
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
