package com.alfiansyah.movieinfodb.data.source.remote.response

import com.google.gson.annotations.SerializedName
import java.math.BigInteger

data class MovieDetailResponse(
        @SerializedName("backdrop_path")
        val backdropPath: String?,
        @SerializedName("genres")
        val genres: List<GenreResponse>,
        @SerializedName("id")
        val id: BigInteger,
        @SerializedName("overview")
        val overview: String?,
        @SerializedName("poster_path")
        val posterPath: String?,
        @SerializedName("release_date")
        val releaseDate: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("vote_average")
        val voteAverage: Double,
)