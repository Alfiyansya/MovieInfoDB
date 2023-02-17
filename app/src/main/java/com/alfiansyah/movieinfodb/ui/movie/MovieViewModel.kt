package com.alfiansyah.movieinfodb.ui.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfiansyah.movieinfodb.data.model.Genre
import com.alfiansyah.movieinfodb.data.model.Movie
import com.alfiansyah.movieinfodb.data.source.repo.AllRepository
import com.alfiansyah.movieinfodb.utils.Result
import com.alfiansyah.movieinfodb.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor( private val repositoryImpl: AllRepository) :
    ViewModel() {
    val loadMovieDataNew: LiveData<State<List<Movie>>> = MutableLiveData()
    val loadMoreMovieDataNew: LiveData<State<List<Movie>>> = MutableLiveData()

    private val movies = arrayListOf<Movie>()
    private var currentPage = 1
    private var job: Job? = null

    private fun loadMovieNew(liveData: MutableLiveData<State<List<Movie>>>, genre: Genre) {
        liveData.postValue(State.Loading())

        job = viewModelScope.launch {
            val result = repositoryImpl.getPageMovieByGenre(
                genre = genre,
                pageNumber = currentPage
            )

            when (result) {
                is Result.Success -> {
                    val page = result.data
                    val content = page.content

                    if (content.isEmpty()) {
                        liveData.postValue(State.EmptyDataFetched())
                    } else {
                        movies.addAll(content)
                        liveData.postValue(State.DataFetched(movies))
                    }
                }
                is Result.Failed -> {
                    val error = result.throwable
                    error.printStackTrace()
                    currentPage -= 1
                    liveData.postValue(State.ErrorOccurred(error.message ?: ""))
                }
            }
        }
    }

    fun loadDataMovie(genre: Genre) {
        currentPage = 1
        loadMovieNew(loadMovieDataNew as MutableLiveData, genre)
    }

    fun loadMoreMovie(genre: Genre) {
        currentPage += 1
        loadMovieNew(loadMoreMovieDataNew as MutableLiveData, genre)
    }

    fun stop() {
        val currentJob = job
        if (currentJob != null && currentJob.isActive) {
            currentJob.cancel()
        }
    }
}