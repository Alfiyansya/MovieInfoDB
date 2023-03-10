package com.alfiansyah.movieinfodb.ui.genre

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.alfiansyah.movieinfodb.R
import com.alfiansyah.movieinfodb.data.model.Genre
import com.alfiansyah.movieinfodb.databinding.FragmentGenreListBinding
import com.alfiansyah.movieinfodb.ui.adapter.GenreListAdapter
import com.alfiansyah.movieinfodb.ui.adapter.OnItemGenreClickCallback
import com.alfiansyah.movieinfodb.ui.movie.MovieListFragmentArgs
import com.alfiansyah.movieinfodb.utils.State
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GenreListFragment : Fragment() {
    private var _binding: FragmentGenreListBinding? = null
    private val binding get() = _binding

    private val genreListViewModel: GenreListViewModel by  viewModels()

    @Inject
     lateinit var genreListAdapter: GenreListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentGenreListBinding = FragmentGenreListBinding.inflate(inflater, container, false)
        _binding = fragmentGenreListBinding
        return binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        initObserver()
    }

    private fun setupRecyclerView() {
        genreListAdapter = GenreListAdapter()
        with(binding?.rvGenre){
            this?.setHasFixedSize(true)
            val layoutManager = GridLayoutManager(requireContext(),3)
            this?.layoutManager = layoutManager
            this?.adapter = genreListAdapter
            genreListAdapter.setOnItemClickCallback(object : OnItemGenreClickCallback{
                override fun onItemClicked(genre: Genre) {
                    val args = MovieListFragmentArgs(genre).toBundle()
                    findNavController().navigate(R.id.genreListToMovie, args)
                }

            })
        }
    }

    private fun initObserver() {
        genreListViewModel.run {
            genreListLiveData.observe(viewLifecycleOwner) { handleLoadGenre(it) }
            binding?.noDataGenreListView?.retryBtn?.setOnClickListener { loadDataGenre() }
        }
    }
    private fun handleLoadGenre(it: State<List<Genre>>) {
        when(it) {
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
                genreListAdapter.setGenre(it.data)
            }
            is State.ErrorOccurred -> {
                showLoadingView(false)
                showNoDataView(true)
                showDataView(false)
            }
        }
    }
    private fun showDataView(show: Boolean) {
        when(show){
            true -> {
                binding?.rvGenre?.visibility = View.VISIBLE
            }
            else -> binding?.rvGenre?.visibility = View.GONE
        }
    }

    private fun showNoDataView(show: Boolean) {
        when(show){
            true -> {
                binding?.infoTV?.text = resources.getString(R.string.check_internet)
                binding?.noDataGenreListView?.root?.visibility = View.VISIBLE
            }
            else -> {
                binding?.infoTV?.text = resources.getString(R.string.genres)
                binding?.noDataGenreListView?.root?.visibility = View.GONE
            }

        }
    }
    private fun showLoadingView(show: Boolean) {
        when(show){
            true -> {
                binding?.loadingGenreListView?.visibility = View.VISIBLE
            }
            else -> binding?.loadingGenreListView?.visibility = View.GONE

        }
    }
    override fun onResume() {
        super.onResume()
        genreListViewModel.loadDataGenre()
    }
    override fun onDestroy() {
        super.onDestroy()
        genreListViewModel.stop()
    }

}