package com.antarikshc.theguardiannews.datasource;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.antarikshc.theguardiannews.model.NewsData;
import com.antarikshc.theguardiannews.util.ConnectAPI;

import java.util.ArrayList;

public class NewsLoader extends AsyncTaskLoader<ArrayList<NewsData>> {

    private String mUrl;

    public NewsLoader(Context context, String mUrl) {
        super(context);
        this.mUrl = mUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    @Override
    public ArrayList<NewsData> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Query the API
        ArrayList<NewsData> news = ConnectAPI.fetchNewsData(mUrl);
        return news;
    }
}
