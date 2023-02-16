package com.alfiansyah.movieinfodb.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ListGenreResponse<T>(
    @SerializedName("status_message")
    val statusMessage: String? = null,
    @SerializedName("genres")
    val genres: List<T>? = null
)