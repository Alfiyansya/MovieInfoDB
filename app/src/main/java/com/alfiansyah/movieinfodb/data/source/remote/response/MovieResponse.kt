package com.alfiansyah.movieinfodb.data.source.remote.response

import com.google.gson.annotations.SerializedName
import java.math.BigInteger

data class MovieResponse(
    @SerializedName("id")
    val id: BigInteger,
    @SerializedName("title")
    val title: String?,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("genre_ids")
    val genreIds: List<Int>,
    @SerializedName("release_date")
    var releaseDate: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("backdrop_path")
    var backDropPath: String?
)