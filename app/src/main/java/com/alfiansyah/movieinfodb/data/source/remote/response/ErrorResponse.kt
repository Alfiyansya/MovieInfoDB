package com.alfiansyah.movieinfodb.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("status_code")
    var statusCode: Int,
    @SerializedName("status_message")
    var statusMessage: String
)