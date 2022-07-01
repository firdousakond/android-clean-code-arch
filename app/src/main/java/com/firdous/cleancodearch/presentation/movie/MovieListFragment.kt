package com.firdous.cleancodearch.presentation.movie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.firdous.cleancodearch.R
import com.firdous.cleancodearch.data.Resource
import com.firdous.cleancodearch.databinding.FragmentMovieListBinding
import com.firdous.cleancodearch.domain.model.Movie
import com.firdous.cleancodearch.util.*
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MovieListFragment : Fragment() {

    private lateinit var binding: FragmentMovieListBinding
    private val viewModel: MovieViewModel by viewModel()
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var scrollListener: RecyclerViewLoadMoreScroll

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            val linearLayoutManager = LinearLayoutManager(requireContext())
            movieAdapter = MovieAdapter { item -> showMovieDetails(item) }
            scrollListener = RecyclerViewLoadMoreScroll(linearLayoutManager)
            with(binding.rvMovies) {
                layoutManager = linearLayoutManager
                adapter = movieAdapter
                addOnScrollListener(scrollListener)
            }
            scrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
                override fun onLoadMore() {
                    if (!scrollListener.getLoaded()) {
                        viewModel.page++
                        movieAdapter.addLoadingView()
                        viewModel.fetchMovies()
                    }
                }
            })
            initObserver()

        }
    }

    private fun initObserver() {
        lifecycleScope.launchWhenStarted {
            viewModel.movieStateFlow.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        if (viewModel.page == 1)
                            binding.progressBar.show()
                    }
                    is Resource.Success -> {
                        binding.progressBar.hide()
                        movieAdapter.removeLoadingView()
                        movieAdapter.addData(resource.data.orEmpty())
                        scrollListener.setLoaded()
                    }
                    is Resource.Error -> {
                        scrollListener.setLoaded()
                        binding.progressBar.hide()
                        activity?.showToast(
                            resource.message ?: getString(R.string.msg_server_error)
                        )
                    }
                }
            }
        }
    }

    private fun showMovieDetails(movie: Movie) {
        Timber.d("OnClick : ${movie.title}")
        findNavController().navigate(
            MovieListFragmentDirections.actionMovieListFragmentToMovieDetailsFragment(
                movie
            )
        )
    }

}