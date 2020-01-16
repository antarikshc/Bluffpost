package com.antarikshc.bluffpost.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antarikshc.bluffpost.R
import com.antarikshc.bluffpost.databinding.FragmentHomeBinding
import com.bumptech.glide.Glide
import javax.inject.Inject

/**
 * A simple [Fragment] subclass with [OnBackPressedDispatcher]
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment HomeFragment.
         */
        @JvmStatic
        fun newInstance() = HomeFragment()

        private val TAG = HomeFragment::class.java.simpleName
    }

    @Inject lateinit var factory: ViewModelProvider.Factory
    private val navController by lazy { findNavController() }
    private val viewModel by lazy { provideHomeViewModel() }
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
        (activity as HomeActivity).homeComponent.inject(this)
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

    private fun provideHomeViewModel() =
        ViewModelProvider(requireActivity(), factory).get(HomeVM::class.java)
}
