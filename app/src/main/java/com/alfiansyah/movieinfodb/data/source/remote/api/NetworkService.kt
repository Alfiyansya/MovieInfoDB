package com.alfiansyah.movieinfodb.data.source.remote.api

import com.alfiansyah.movieinfodb.data.source.remote.response.GenreResponse
import com.alfiansyah.movieinfodb.data.source.remote.response.ListGenreResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {

    @GET("genre/movie/list")
    fun getGenres(
        @Query("api_key") apiKey: String,
        @Query("language") language: String): Call<ListGenreResponse<GenreResponse>>


}