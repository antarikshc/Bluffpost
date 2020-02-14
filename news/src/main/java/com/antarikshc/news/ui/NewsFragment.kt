package com.antarikshc.news.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antarikshc.bluffpost.ui.BaseApplication
import com.antarikshc.news.R
import com.antarikshc.news.databinding.FragmentHomeBinding
import com.antarikshc.news.di.DaggerNewsComponent
import com.antarikshc.news.di.NewsComponent
import com.bumptech.glide.Glide
import javax.inject.Inject

/**
 * A simple [Fragment] subclass with [OnBackPressedDispatcher]
 * Use the [NewsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewsFragment : Fragment() {

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment HomeFragment.
         */
        @JvmStatic
        fun newInstance() = NewsFragment()

        private val TAG = NewsFragment::class.java.simpleName
    }

    lateinit var newsComponent: NewsComponent
    @Inject lateinit var viewModel: NewsVM
    private val navController by lazy { findNavController() }
    private val glide by lazy { Glide.with(this) }
    private lateinit var binding: FragmentHomeBinding
    private var adapter: NewsAdapter? = null
    private var scrollToTop = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) { onBackPressed() }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Obtaining the Home graph from Activity and instantiate
        // the @Inject fields with objects from the graph

        val appComponent = (context as BaseApplication).appComponent
        newsComponent = DaggerNewsComponent.factory().create(appComponent)
        newsComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = setupNewsRV()

        setupSwipeRefresh()

        viewModel.news.observe(viewLifecycleOwner, Observer {
            adapter?.swapData(it)
            scrollToStart()
            binding.swipeRefreshHome.isRefreshing = false
        })
    }

    private fun setupNewsRV(): NewsAdapter {
        val adapter = NewsAdapter(glide)
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.recyclerHomeNews.layoutManager = layoutManager
        binding.recyclerHomeNews.adapter = adapter

        return adapter
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshHome.setOnRefreshListener {
            scrollToTop = true
            viewModel.refresh()
        }
    }

    private fun scrollToStart() {
        if (!adapter?.currentList.isNullOrEmpty() && scrollToTop) {
            binding.recyclerHomeNews.layoutManager
                ?.smoothScrollToPosition(binding.recyclerHomeNews, RecyclerView.State(), 0)
            scrollToTop = false
        }
    }

    /**
     * Intercepts Back Pressed to delegate responsibility to Fragment
     * Do onBackPressed action here
     */
    private fun onBackPressed() {
        requireActivity().finish()
    }

}
