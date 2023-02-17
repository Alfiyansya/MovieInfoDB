package com.alfiansyah.movieinfodb.ui.movie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.alfiansyah.movieinfodb.R
import com.alfiansyah.movieinfodb.data.model.Genre
import com.alfiansyah.movieinfodb.data.model.Movie
import com.alfiansyah.movieinfodb.databinding.FragmentMovieListBinding
import com.alfiansyah.movieinfodb.ui.adapter.MovieAdapter
import com.alfiansyah.movieinfodb.ui.adapter.OnItemMovieClickCallback
import com.alfiansyah.movieinfodb.ui.adapter.PagingAdapter
import com.alfiansyah.movieinfodb.ui.detail.DetailMovieFragmentArgs
import com.alfiansyah.movieinfodb.utils.ScrollListener
import com.alfiansyah.movieinfodb.utils.State
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MovieListFragment : Fragment() {
    private var _binding:FragmentMovieListBinding?=null
    private val binding get() = _binding

    private val movieViewModel: MovieViewModel by viewModels()

    @Inject
     lateinit var movieListAdapter: MovieAdapter

    private lateinit var scrollListener: ScrollListener
    private lateinit var genre : Genre

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentMovieBinding = FragmentMovieListBinding.inflate(inflater, container, false)
        _binding = fragmentMovieBinding
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: MovieListFragmentArgs by navArgs()
        val receivedGenre = args.genre
        findNavController().currentDestination?.label = receivedGenre.name
        genre = receivedGenre

        setupRecyclerView(receivedGenre)
        initObserver(receivedGenre)

    }
    private fun setupRecyclerView(genre: Genre) {
        movieListAdapter = MovieAdapter()
        with(binding?.rvMovie){
            this?.setHasFixedSize(true)
            this?.adapter = movieListAdapter
            val layoutManager = GridLayoutManager(requireContext(),2)
            this?.layoutManager = layoutManager

            scrollListener = ScrollListener(layoutManager, object : ScrollListener.OnLoadMoreListener {
                override fun onLoadMoreItems() {
                    movieViewModel.loadMoreMovie(genre)
                }
            })

            this?.addOnScrollListener(scrollListener)
        }
        movieListAdapter.onRetryLoadMoreListener = object : PagingAdapter.OnRetryLoadMoreListener {
            override fun onRetryLoadMore() {
                movieViewModel.loadMoreMovie(genre)
            }
        }
        movieListAdapter.setOnItemClickCallback(object : OnItemMovieClickCallback{
            override fun onItemClicked(movie: Movie) {
                val args = DetailMovieFragmentArgs(movieId = movie.id).toBundle()
                findNavController().navigate(R.id.movieListToDetailMovie,args)
            }

        })

    }

    override fun onResume() {
        super.onResume()
        movieViewModel.loadDataMovie(genre)
    }
    private fun initObserver(genre: Genre) {
        movieViewModel.loadDataMovie(genre)
        movieViewModel.run {
            loadMovieDataNew.observe(viewLifecycleOwner) {
                handleFirstLoadMovie(it)
            }
            loadMoreMovieDataNew.observe(viewLifecycleOwner) {
                handleLoadMoreMovie(it)
            }
            binding?.noDataMovieListView?.retryBtn?.setOnClickListener {
                loadDataMovie(genre)
            }
        }
    }
    private fun handleFirstLoadMovie(it: State<List<Movie>>?) {
        when (it) {
            is State.Loading -> {
                showNoDataView(false)
                showLoadingView(true)
            }
            is State.EmptyDataFetched -> {
                showLoadingView(false)
                showNoDataView(true)
                showDataView(false)
            }
            is State.DataFetched -> {
                showLoadingView(false)
                showDataView(true)
                showNoDataView(false)
                movieListAdapter.changeData(it.data)
            }
            is State.ErrorOccurred -> {
                showLoadingView(false)
                showNoDataView(true)
            }
        }
    }
    private fun handleLoadMoreMovie(it: State<List<Movie>>?) {
        when (it) {
            is State.Loading -> {
                scrollListener.isLastPage = false
                scrollListener.isLoading = true
                movieListAdapter.showLoading()
            }
            is State.EmptyDataFetched -> {
                scrollListener.isLoading = false
                movieListAdapter.closeLoading()

                scrollListener.isLastPage = true
                movieListAdapter.showNoMoreData()
            }
            is State.DataFetched -> {
                scrollListener.isLoading = false
                movieListAdapter.closeLoading()
                movieListAdapter.changeData(it.data)
            }
            is State.ErrorOccurred -> {
                scrollListener.isLoading = false
                movieListAdapter.closeLoading()
                scrollListener.isLastPage = true
                movieListAdapter.showError()
            }
        }
    }
    private fun showDataView(show: Boolean) {
        when(show){
            true -> {
                binding?.rvMovie?.visibility = View.VISIBLE
            }
            false -> binding?.rvMovie?.visibility = View.GONE
        }
    }

    private fun showNoDataView(show: Boolean) {
        when(show){
            true -> {
                binding?.noDataMovieListView?.root?.visibility = View.VISIBLE
            }
            else -> {
                binding?.noDataMovieListView?.root?.visibility = View.GONE
            }

        }
    }
    private fun showLoadingView(show: Boolean) {
        when(show){
            true -> {
                binding?.loadingMovieListView?.visibility = View.VISIBLE
            }
            else -> binding?.loadingMovieListView?.visibility = View.GONE

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        movieViewModel.stop()
    }
}