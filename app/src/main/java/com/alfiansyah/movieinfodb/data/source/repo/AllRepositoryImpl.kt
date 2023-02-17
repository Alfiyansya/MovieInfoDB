package com.alfiansyah.movieinfodb.data.source.repo

import com.alfiansyah.movieinfodb.BuildConfig
import com.alfiansyah.movieinfodb.data.model.*
import com.alfiansyah.movieinfodb.data.source.remote.api.NetworkService
import com.alfiansyah.movieinfodb.data.source.remote.response.ErrorResponse
import com.alfiansyah.movieinfodb.utils.*
import retrofit2.HttpException
import retrofit2.await
import java.math.BigInteger
import java.net.UnknownHostException
import javax.inject.Inject

open class AllRepositoryImpl @Inject constructor(private val networkService: NetworkService) :
    AllRepository {
    override suspend fun getGenres(): Result<List<Genre>> {
        return try {
            val service = networkService.getGenres(
                apiKey = BuildConfig.TMDB_API_KEY,
                language = BuildConfig.TMDB_LANGUAGE
            ).await()
            val genres = MapperList.map(service.genres) {
                Genre.from(it)
            }
            Result.Success(genres)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    override suspend fun getPageMovieByGenre(genre: Genre, pageNumber: Int): Result<Page<Movie>> {
        return try {
            val service = networkService.getMoviesByGenre(
                apiKey = BuildConfig.TMDB_API_KEY,
                language = BuildConfig.TMDB_LANGUAGE,
                page = pageNumber,
                genreId = genre.id.toString()
            ).await()
            val movies = MapperList.map(service.results) {
                Movie.from(it)
            }

            val page = Page(
                content = movies,
                page = service.page,
                totalPages = service.totalPages,
                totalResults = service.totalResults
            )

            Result.Success(page)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    override suspend fun getDetailMovieById(movieId: BigInteger): Result<Movie> {
        return try {
            val service = networkService.getDetailMovie(
                apiKey = BuildConfig.TMDB_API_KEY,
                language = BuildConfig.TMDB_LANGUAGE,
                movieId = movieId
            ).await()
            val movie = Movie.from(service)
            Result.Success(movie)
        }catch (e: Exception){
            handleException(e)
        }
    }

    override suspend fun getReviewsMovie(movie: Movie, pageNumber: Int): Result<Page<Review>> {
        return try {
            val service = networkService.getMovieReviews(
                apiKey = BuildConfig.TMDB_API_KEY,
                language = BuildConfig.TMDB_LANGUAGE,
                page = pageNumber,
                movieId = movie.id
            ).await()
            val review = MapperList.map(service.results) {
                Review.from(it, movie)
            }

            val page = Page(
                content = review,
                page = service.page,
                totalPages = service.totalPages,
                totalResults = service.totalResults
            )

            Result.Success(page)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    override suspend fun getVideosMovie(movie: Movie): Result<List<Video>> {
        return try {
            val service = networkService.getMovieTrailer(
                apiKey = BuildConfig.TMDB_API_KEY,
                language = BuildConfig.TMDB_LANGUAGE,
                movieId = movie.id
            ).await()
            val video = MapperList.map(service.results) {
                Video.from(it,movie)
            }
            Result.Success(video)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    private fun <T> handleException(e: Throwable): Result.Failed<T> {
        val throwable: Throwable =
            if (e is HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                if (errorBody != null) {
                    val errorResponse = JSonHelper.fromJson(ErrorResponse::class.java, errorBody)
                    ApiException(errorResponse.statusMessage)
                } else {
                    ApiException("Unable to fetch data caused by ${e.message()}")
                }
            } else if (e is UnknownHostException) {
                ConnectException("Unable to connect to server, please check your internet connection")
            } else {
                SystemException("System error caused by ${e.message}")
            }

        return Result.Failed(throwable)
    }

}