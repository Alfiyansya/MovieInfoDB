package com.alfiansyah.movieinfodb.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ListGenreResponse(
    @SerializedName("genres")
    val genres: List<GenreResponse>
)