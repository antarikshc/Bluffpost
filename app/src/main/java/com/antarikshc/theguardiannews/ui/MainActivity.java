package com.antarikshc.theguardiannews.ui;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.antarikshc.theguardiannews.R;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getName();

    /**
     * API KEY
     **/
    private static String API_KEY = "753d66b9-55a1-4196-bc18-57c05d86c5ce";

    /**
     * global declarations
     **/
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    SearchView searchView;
    LinearLayout searchBar;
    MenuItem item;

    WorldFragment tab1;
    PoliticsFragment tab2;
    SportsFragment tab3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

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

                if (checkNet()) {

                    item.collapseActionView();

                    //Clear focus so that search bar can be translated back up top
                    searchView.setQuery("", false);
                    searchView.clearFocus();
                    searchBar.clearFocus();

                    //create URL for the search query
                    Uri baseUri = Uri.parse("https://content.guardianapis.com/search");
                    Uri.Builder uriBuilder = baseUri.buildUpon();

                    uriBuilder.appendQueryParameter("q", query);
                    uriBuilder.appendQueryParameter("format", "json");
                    uriBuilder.appendQueryParameter("page-size", "8");
                    uriBuilder.appendQueryParameter("from-date", "2017-01-01");
                    uriBuilder.appendQueryParameter("show-fields", "thumbnail,headline,byline");
                    uriBuilder.appendQueryParameter("show-tags", "contributor");
                    uriBuilder.appendQueryParameter("api-key", API_KEY);

                    Intent searchIntent = new Intent(getApplicationContext(), SearchActivity.class);
                    searchIntent.putExtra("title", query);
                    searchIntent.putExtra("url", uriBuilder.toString());
                    startActivity(searchIntent);

                } else {
                    Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    searchView.setIconified(true);
                }
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
        mViewPager.requestFocus();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    tab1 = new WorldFragment();
                    tab1.setRetainInstance(true);
                    return tab1;
                case 1:
                    tab2 = new PoliticsFragment();
                    tab2.setRetainInstance(true);
                    return tab2;
                case 2:
                    tab3 = new SportsFragment();
                    tab3.setRetainInstance(true);
                    return tab3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
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
