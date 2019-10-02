package com.antarikshc.theguardiannews.ui

import android.animation.LayoutTransition
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import com.antarikshc.theguardiannews.R
import com.antarikshc.theguardiannews.util.Master
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // Global params
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private lateinit var mViewPager: ViewPager
    private lateinit var tab1: WorldFragment
    private lateinit var tab2: PoliticsFragment
    private lateinit var tab3: SportsFragment
    private lateinit var searchView: SearchView
    private lateinit var searchBar: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        // Set up the ViewPager with the sections adapter.
        val mViewPagerLayout = container
        mViewPagerLayout.adapter = mSectionsPagerAdapter

        val tabLayout = tabs

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(mViewPagerLayout))

    }

    /**
     * Setting up Search bar in Toolbar
     *
     * @param menu Search item
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_search, menu)
        val item = menu.findItem(R.id.app_bar_search)
        val searchView = item.actionView as SearchView
        searchView.maxWidth = Integer.MAX_VALUE

        val searchBarId = searchView.context.resources.getIdentifier("android:id/search_bar", null, null)
        searchBar = searchView.findViewById(searchBarId)
        searchBar.layoutTransition = LayoutTransition()

        // Setup Query listener to perform searching operation
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                if (Master.checkNet(this@MainActivity)) {

                    item.collapseActionView()

                    // Clear focus so that search bar can be translated back up top
                    searchView.setQuery("", false)
                    searchView.clearFocus()
                    searchBar.clearFocus()

                    // Create URL for the search query
                    val uriBuilder = Master.searchUri

                    uriBuilder.appendQueryParameter("q", query)
                    uriBuilder.appendQueryParameter("format", "json")
                    uriBuilder.appendQueryParameter("page-size", "8")
                    uriBuilder.appendQueryParameter("from-date", "2017-01-01")
                    uriBuilder.appendQueryParameter("show-fields", "thumbnail,headline,byline")
                    uriBuilder.appendQueryParameter("show-tags", "contributor")
                    uriBuilder.appendQueryParameter("api-key", Master.apiKey)

                    val searchIntent = Intent(applicationContext, SearchActivity::class.java)
                    searchIntent.putExtra("title", query)
                    searchIntent.putExtra("url", uriBuilder.toString())
                    startActivity(searchIntent)

                } else {
                    Toast.makeText(this@MainActivity, "No Internet Connection", Toast.LENGTH_SHORT).show()
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                searchView.isIconified = true
            }
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
        //we don't want search bar in focus when user returns to Main screen
        if (::searchView.isInitialized) {
            searchView.setQuery("", false)
            searchView.clearFocus()
            searchBar.clearFocus()
            mViewPager.requestFocus()
        }
    }

    /**
     * Boilerplate to setup Tabs
     */
    inner class SectionsPagerAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            when (position) {
                0 -> {
                    tab1 = WorldFragment()
                    tab1.retainInstance = true
                    return tab1
                }
                1 -> {
                    tab2 = PoliticsFragment()
                    tab2.retainInstance = true
                    return tab2
                }
                2 -> {
                    tab3 = SportsFragment()
                    tab3.retainInstance = true
                    return tab3
                }
                else -> return null
            }
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 3
        }
    }
}
