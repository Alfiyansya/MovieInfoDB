package com.alfiansyah.movieinfodb.data.source.repo

import com.alfiansyah.movieinfodb.BuildConfig
import com.alfiansyah.movieinfodb.data.entity.Genre
import com.alfiansyah.movieinfodb.data.source.remote.api.NetworkService
import com.alfiansyah.movieinfodb.utils.AnyException
import com.alfiansyah.movieinfodb.utils.DataMapper
import com.alfiansyah.movieinfodb.utils.Result
import retrofit2.HttpException
import retrofit2.await
import java.net.UnknownHostException
import javax.inject.Inject

class AllRepositoryImpl @Inject constructor(private val networkService: NetworkService):
    AllRepository {
    private lateinit var genre : List<Genre>
    override suspend fun getGenres(): Result<List<Genre>> {
        return try {
            networkService.getGenres(apiKey = BuildConfig.TMDB_API_KEY, language = BuildConfig.TMDB_LANGUAGE)
                .await().genres?.let { listGenre ->
                    val genres = DataMapper.mapGenreResponsesToModel(listGenre)
                    genre = genres
                }
            Result.Success(genre)
        } catch (e: Exception) {
            handleException(e)
        }
    }
    private suspend fun <T> handleException(e: Throwable): Result.Failed<T> {
        val throwable: Throwable =
            if (e is HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                if (errorBody != null) {
//                    val errorResponse = JSonHelper.fromJson(ErrorResponse::class.java, errorBody)
                    val errorResponse = networkService.getGenres(apiKey = BuildConfig.TMDB_API_KEY, language = BuildConfig.TMDB_LANGUAGE)

                    AnyException(errorResponse.await().statusMessage)
                } else {
                    AnyException("Unable to fetch data caused by ${e.message()}")
                }
            } else if (e is UnknownHostException) {
                AnyException("Unable to connect to server, please check your internet connection")
            } else {
                AnyException("System error caused by ${e.message}")
            }

        return Result.Failed(throwable)
    }
}