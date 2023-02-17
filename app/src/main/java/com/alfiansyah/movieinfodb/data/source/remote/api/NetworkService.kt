package com.alfiansyah.movieinfodb.data.source.remote.api

import com.alfiansyah.movieinfodb.data.source.remote.response.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.math.BigInteger

interface NetworkService {

    @GET("genre/movie/list")
    fun getGenres(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<ListGenreResponse>

    @GET("discover/movie")
    fun getMoviesByGenre(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("with_genres") genreId: String
    ): Call<PageResponse<MovieResponse>>

    @GET("movie/{movieId}")
    fun getDetailMovie(
        @Path("movieId") movieId: BigInteger,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<MovieDetailResponse>

    @GET("movie/{movieId}/reviews")
     fun getMovieReviews(
        @Path("movieId") movieId: BigInteger,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<PageResponse<ReviewResponse>>

    @GET("movie/{movieId}/videos")
    fun getMovieTrailer(
        @Path("movieId") movieId: BigInteger,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<ListVideoResponse>
}