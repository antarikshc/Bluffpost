package com.antarikshc.theguardiannews;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;

public class NewsLoader extends AsyncTaskLoader<ArrayList<NewsData>> {

    private String mUrl;

    NewsLoader(Context context, String mUrl) {
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

        ArrayList<NewsData> news = ConnectAPI.fetchNewsData(mUrl);
        return news;
    }
}
