package com.alfiansyah.movieinfodb.ui.genre

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfiansyah.movieinfodb.data.model.Genre
import com.alfiansyah.movieinfodb.data.source.repo.AllRepository
import com.alfiansyah.movieinfodb.utils.Result
import com.alfiansyah.movieinfodb.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenreListViewModel @Inject constructor( private val repository: AllRepository): ViewModel() {
    val genreListLiveData: LiveData<State<List<Genre>>> = MutableLiveData()
    private var job: Job? = null

    fun loadDataGenre() {
        val liveData = genreListLiveData as MutableLiveData
        liveData.postValue(State.Loading())

        job = viewModelScope.launch {
            when(val result = repository.getGenres()) {
                is Result.Success -> {
                    val genres = result.data

                    if (genres.isEmpty()) {
                        liveData.postValue(State.EmptyDataFetched())
                    } else {
                        liveData.postValue(State.DataFetched(genres))
                    }
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