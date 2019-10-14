package com.antarikshc.theguardiannews.ui

import android.animation.LayoutTransition
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.antarikshc.theguardiannews.R
import com.antarikshc.theguardiannews.util.Master
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // Global params
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private lateinit var mViewPager: androidx.viewpager.widget.ViewPager
    private val tabs: Array<Fragment> = arrayOf(WorldFragment(), PoliticsFragment(), SportsFragment())
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

        val tabLayout = tabLayout

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
                            .putExtra("title", query)
                            .putExtra("url", uriBuilder.toString())
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
    inner class SectionsPagerAdapter internal constructor(manager: FragmentManager)
        : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            tabs[position].retainInstance = true
            return tabs[position]
        }

        // Hardcoding, because 3 tabs
        override fun getCount(): Int = tabs.size
    }
}
