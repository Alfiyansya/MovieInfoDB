package com.alfiansyah.movieinfodb.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ListVideoResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("results")
    val results: List<VideoResponse>
)