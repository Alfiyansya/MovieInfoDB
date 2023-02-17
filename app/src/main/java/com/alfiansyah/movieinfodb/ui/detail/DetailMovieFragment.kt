package com.alfiansyah.movieinfodb.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.alfiansyah.movieinfodb.R
import com.alfiansyah.movieinfodb.data.model.Movie
import com.alfiansyah.movieinfodb.data.model.Review
import com.alfiansyah.movieinfodb.data.model.Video
import com.alfiansyah.movieinfodb.databinding.FragmentDetailMovieBinding
import com.alfiansyah.movieinfodb.ui.adapter.ItemReviewAdapter
import com.alfiansyah.movieinfodb.ui.adapter.ItemVideoAdapter
import com.alfiansyah.movieinfodb.ui.adapter.OnItemVideoClickCallback
import com.alfiansyah.movieinfodb.ui.adapter.PagingAdapter
import com.alfiansyah.movieinfodb.utils.DateFormatterHelper
import com.alfiansyah.movieinfodb.utils.ScrollListener
import com.alfiansyah.movieinfodb.utils.State
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigInteger
import java.util.*
import javax.inject.Inject

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class DetailMovieFragment : Fragment() {
    private var _binding: FragmentDetailMovieBinding? = null
    private val binding get() = _binding

    private val detailViewModel: DetailViewModel by viewModels()

    @Inject
    lateinit var videoAdapter: ItemVideoAdapter

    @Inject
    lateinit var reviewAdapter: ItemReviewAdapter

    private lateinit var scrollListener: ScrollListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val fragmentDetailMovieBinding =
            FragmentDetailMovieBinding.inflate(inflater, container, false)
        _binding = fragmentDetailMovieBinding
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: DetailMovieFragmentArgs by navArgs()
        val movieId = args.movieId
        initObserver(movieId)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding?.movieReviewView?.run {
            val layoutManagerReview = LinearLayoutManager(requireContext())
            scrollListener =
                ScrollListener(layoutManagerReview, object : ScrollListener.OnLoadMoreListener {
                    override fun onLoadMoreItems() {
                        detailViewModel.loadMoreReviews()
                    }
                })
            reviewAdapter = ItemReviewAdapter()
            reviewAdapter.onRetryLoadMoreListener = object : PagingAdapter.OnRetryLoadMoreListener {
                override fun onRetryLoadMore() {
                    detailViewModel.reloadReviews()
                }
            }
            reviewRv.layoutManager = layoutManagerReview
            reviewRv.adapter = reviewAdapter
            noDataView.retryBtn.setOnClickListener { detailViewModel.reloadReviews() }
        }

        binding?.movieVideosView?.run {
            val layoutManagerVideos = LinearLayoutManager(requireContext())
            videoAdapter = ItemVideoAdapter()
            videoAdapter.setOnItemClickCallback(object : OnItemVideoClickCallback {

                @SuppressLint("SetTextI18n")
                override fun onItemClicked(video: Video) {
                    if (video.site.equals("Youtube", ignoreCase = true)) {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse("https://www.youtube.com/watch?v=${video.key}")
                        startActivity(intent)
                    } else {
                        val bottomSheetDialog = BottomSheetDialog(requireContext())
                        bottomSheetDialog.setContentView(R.layout.error_dialog)
                        bottomSheetDialog.findViewById<TextView>(R.id.messageError)?.text = "\"Showing video from ${video.site} is not supported\""
                        bottomSheetDialog.show()
                    }
                }
            })

            rvTrailer.adapter = videoAdapter
            rvTrailer.layoutManager = layoutManagerVideos
            noDataView.retryBtn.setOnClickListener { detailViewModel.reloadDataVideos() }
        }
    }

    private fun initObserver(movieId: BigInteger) {
        detailViewModel.loadDetailMovie(movieId)
        detailViewModel.run {
            loadDetailMovieLiveData.observe(viewLifecycleOwner) { handleDetailData(it) }
            loadVideosLiveData.observe(viewLifecycleOwner) { handleVideoState(it) }
            loadReviewLiveData.observe(viewLifecycleOwner) { handleReviewState(it) }
            loadMoreReviewLiveData.observe(viewLifecycleOwner) { handleLoadMoreReviewState(it) }
        }
    }
    private fun handleReviewState(state: State<List<Review>>) {
        when (state) {
            is State.Loading -> showLoadingReview(true)
            is State.EmptyDataFetched -> {
                showLoadingReview(false)
                showDataViewForReview(false)
            }
            is State.DataFetched -> {
                showLoadingReview(false)
                showDataViewForReview(true)
                reviewAdapter.changeData(state.data)
            }
            is State.ErrorOccurred -> {
                showLoadingReview(false)
                showDataViewForReview(false)
                val bottomSheetDialog = BottomSheetDialog(requireContext())
                bottomSheetDialog.setContentView(R.layout.error_dialog)
                bottomSheetDialog.findViewById<TextView>(R.id.messageError)?.text = "Please Check your internet connection"
                bottomSheetDialog.show()
            }
        }
    }

    private fun handleLoadMoreReviewState(state: State<List<Review>>) {
        when (state) {
            is State.Loading -> {
                scrollListener.isLoading = true
                reviewAdapter.showLoading()
            }
            is State.EmptyDataFetched -> {
                scrollListener.isLoading = false
                scrollListener.isLastPage = true
                reviewAdapter.closeLoading()
            }
            is State.DataFetched -> {
                scrollListener.isLoading = false
                reviewAdapter.closeLoading()
                reviewAdapter.changeData(state.data)
            }
            is State.ErrorOccurred -> {
                scrollListener.isLoading = false
                scrollListener.isLastPage = true
                reviewAdapter.closeLoading()

            }
        }
    }

    private fun handleVideoState(state: State<List<Video>>) {
        when (state) {
            is State.Loading -> showLoadingVideos(true)
            is State.EmptyDataFetched -> {
                showLoadingVideos(false)
                showDataViewForVideos(false)
            }
            is State.DataFetched -> {
                showLoadingVideos(false)
                showDataViewForVideos(true)
                videoAdapter.setVideo(state.data)
            }
            is State.ErrorOccurred -> {
                showLoadingVideos(false)
                showDataViewForVideos(false)
                val bottomSheetDialog = BottomSheetDialog(requireContext())
                bottomSheetDialog.setContentView(R.layout.error_dialog)
                bottomSheetDialog.findViewById<TextView>(R.id.messageError)?.text = "Please Check your internet connection"
                bottomSheetDialog.show()
            }
        }
    }


    private fun handleDetailData(state: State<Movie>) {
        when (state) {
            is State.Loading -> showLoadingDetailView(true)
            is State.EmptyDataFetched -> {
                showLoadingDetailView(false)
                showNoDataDetailView(true)
            }
            is State.DataFetched -> {
                showLoadingDetailView(false)
                showNoDataDetailView(false)
                showDataDetailMovie(state.data)
            }
            is State.ErrorOccurred -> {
                showLoadingDetailView(false)
                showNoDataDetailView(true)
                val bottomSheetDialog = BottomSheetDialog(requireContext())
                bottomSheetDialog.setContentView(R.layout.error_dialog)
                bottomSheetDialog.findViewById<TextView>(R.id.messageError)?.text = "Please Check your internet connection"
                bottomSheetDialog.show()
            }
        }
    }

    private fun showDataDetailMovie(movie: Movie?) {
        val none = getString(R.string.none)
        binding?.run {
            val imagePoster = movie?.movieImage?.posterPath
            val imageBackdrop = movie?.movieImage?.backDropPath
            Picasso.get().load(imagePoster)
                .into(detailPoster)
            Picasso.get().load(imageBackdrop)
                .into(detailBackdrop)

            detailTitle.text = movie?.title ?: none

            val releaseDate = movie?.releaseDate
            detailReleaseDate.text =
                if (releaseDate != null) DateFormatterHelper.format(releaseDate) else none

            val voteAverage = movie?.vote?.average
            detailRatingBar.rating = if (voteAverage != null) (voteAverage / 2).toFloat() else 0F

            detailGenre.text = movie?.genres?.joinToString { it.name } ?: none
            detailRating.text = voteAverage?.toString() ?: none
            detailOverviewValue.text = movie?.overview ?: none
        }
    }

    private fun showNoDataDetailView(show: Boolean) {
        if (show) {
            showDataDetailMovie(null)
        }

    }

    private fun showLoadingDetailView(show: Boolean) {
        when (show) {
            true -> binding?.detailAnimLoader?.visibility = View.VISIBLE
            else -> binding?.detailAnimLoader?.visibility = View.GONE

        }
    }
    private fun showDataViewForReview(show: Boolean) {
        when(show){
            true -> {
                binding?.movieReviewView?.root?.visibility =View.VISIBLE
                binding?.movieReviewView?.noDataView?.root?.visibility =View.GONE
            }
            false -> {
                binding?.movieReviewView?.noDataView?.root?.visibility =View.VISIBLE
            }
        }
    }

    private fun showLoadingReview(show: Boolean) {
        when(show){
            true -> binding?.movieReviewView?.loadingReviewAnimLoader?.visibility =View.VISIBLE
            false -> binding?.movieReviewView?.loadingReviewAnimLoader?.visibility =View.GONE
        }
    }

    private fun showDataViewForVideos(show: Boolean) {
        when(show){
            true -> {
                binding?.movieVideosView?.root?.visibility =View.VISIBLE
                binding?.movieVideosView?.noDataView?.root?.visibility =View.GONE
            }
            false -> {
                binding?.movieVideosView?.noDataView?.root?.visibility =View.VISIBLE
            }
        }
    }

    private fun showLoadingVideos(show: Boolean) {
        when(show){
            true -> binding?.movieVideosView?.loadingVideoAnimLoader?.visibility =View.VISIBLE
            false -> binding?.movieVideosView?.loadingVideoAnimLoader?.visibility =View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        detailViewModel.stop()
    }
}