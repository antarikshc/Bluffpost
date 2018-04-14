package com.antarikshc.theguardiannews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<NewsData>> {

    public static final String LOG_TAG = MainActivity.class.getName();

    /**
     * URL to fetch data
     **/
    private static String NEWS_API_URL = "https://content.guardianapis.com/search?tags=international&format=json&page-size=8&from-date=2017-03-01&show-fields=thumbnail,headline&order-by=newest&show-references=author&api-key=753d66b9-55a1-4196-bc18-57c05d86c5ce";

    //Books loaded ID, default = 1 currently using single Loader
    private static int NEWS_LOADER_ID = 1;

    /**
     * global declarations
     **/
    ListView newsListView;
    private CustomAdapter customAdapter;
    LoaderManager loaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsListView = findViewById(R.id.newsList);

        //initiate CustomAdapter and set it for newsListView
        customAdapter = new CustomAdapter(getApplicationContext(), new ArrayList<NewsData>());
        newsListView.setAdapter(customAdapter);

        loaderManager = getLoaderManager();

        boolean netConnection = checkNet();
        if (netConnection) {
            //we need to call Loader in onCreate
            //else it won't persist through orientations
            executeLoader();
        }
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
        return new NewsLoader(this, NEWS_API_URL);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<NewsData>> loader, ArrayList<NewsData> news) {
        // Clear the adapter of previous books data
        customAdapter.clear();

        if (news != null && !news.isEmpty()) {
            customAdapter.addAll(news);
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
