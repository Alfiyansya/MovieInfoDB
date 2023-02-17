package com.alfiansyah.movieinfodb.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfiansyah.movieinfodb.data.model.Movie
import com.alfiansyah.movieinfodb.data.model.Review
import com.alfiansyah.movieinfodb.data.model.Video
import com.alfiansyah.movieinfodb.data.source.repo.AllRepository
import com.alfiansyah.movieinfodb.utils.Result
import com.alfiansyah.movieinfodb.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.math.BigInteger
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private var repository: AllRepository): ViewModel() {
    val loadDetailMovieLiveData: LiveData<State<Movie>> = MutableLiveData()
    val loadVideosLiveData: LiveData<State<List<Video>>> = MutableLiveData()
    val loadReviewLiveData: LiveData<State<List<Review>>> = MutableLiveData()
    val loadMoreReviewLiveData: LiveData<State<List<Review>>> = MutableLiveData()
    private var currentMovie: Movie? = null
    private var job: Job? = null
    private val currentReviews = arrayListOf<Review>()
    private var currentPageReview = 1

    private suspend fun loadVideos(movie: Movie?) {
        val liveData = loadVideosLiveData as MutableLiveData
        liveData.postValue(State.Loading())

        if (movie != null) {
            when(val result = repository.getVideosMovie(movie)) {
                is Result.Success -> {
                    val videos = result.data

                    if (videos.isEmpty()) {
                        liveData.postValue(State.EmptyDataFetched())
                    } else {
                        liveData.postValue(State.DataFetched(videos))
                    }
                }
                is Result.Failed -> {
                    val error = result.throwable
                    error.printStackTrace()
                    liveData.postValue(State.ErrorOccurred(error.message ?: ""))
                }
            }
        } else {
            liveData.postValue(State.ErrorOccurred("No data movie has been loaded"))
        }
    }

    private suspend fun loadReviews(movie: Movie?, liveData: MutableLiveData<State<List<Review>>>) {

        if (movie != null) {
            when(val result = repository.getReviewsMovie(movie, currentPageReview)) {
                is Result.Success -> {
                    val page = result.data
                    val reviews = page.content

                    if (reviews.isEmpty()) {
                        liveData.postValue(State.EmptyDataFetched())
                    } else {
                        currentReviews.addAll(reviews)
                        liveData.postValue(State.DataFetched(currentReviews))
                    }
                }
                is Result.Failed -> {
                    val error = result.throwable
                    error.printStackTrace()
                    liveData.postValue(State.ErrorOccurred(error.message ?: ""))
                    currentPageReview -= 1
                }
            }
        } else {
            liveData.postValue(State.ErrorOccurred("No data movie has been loaded"))
        }
    }

    fun reloadDataVideos() {
        job = viewModelScope.launch {
            loadVideos(currentMovie)
        }
    }

    fun reloadReviews() {
        currentReviews.clear()
        currentPageReview = 1

        job = viewModelScope.launch {
            loadReviews(currentMovie, loadReviewLiveData as MutableLiveData)
        }
    }

    fun loadMoreReviews() {
        currentPageReview += 1
        job = viewModelScope.launch {
            loadReviews(currentMovie, loadMoreReviewLiveData as MutableLiveData)
        }
    }

    fun loadDetailMovie(movieId : BigInteger) {
        val liveData = loadDetailMovieLiveData as MutableLiveData

        liveData.postValue(State.Loading())
        job = viewModelScope.launch {
            when(val result = repository.getDetailMovieById(movieId)) {
                is Result.Success -> {
                    val movie = result.data
                    currentMovie = movie
                    liveData.postValue(State.DataFetched(movie))
                    loadVideos(movie)
                    loadReviews(movie, loadReviewLiveData as MutableLiveData)
                }
                is Result.Failed -> {
                    val error = result.throwable
                    error.printStackTrace()
                    liveData.postValue(State.ErrorOccurred(error.message ?: ""))
                }
            }
        }
    }

    fun stop() {
        val currentJob = job
        if (currentJob != null && currentJob.isActive) {
            currentJob.cancel()
        }
    }

}