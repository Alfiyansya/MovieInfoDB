package com.alfiansyah.movieinfodb.data.model

import com.alfiansyah.movieinfodb.data.source.remote.response.MovieDetailResponse
import com.alfiansyah.movieinfodb.data.source.remote.response.MovieResponse

data class Vote(
        var average: Double,
) {
    companion object {
        fun from(movieDetailResponse: MovieDetailResponse): Vote {
            return Vote(
                    average = movieDetailResponse.voteAverage
            )
        }

        fun from(movieResponse: MovieResponse): Vote {
            return Vote(
                    average = movieResponse.voteAverage,
            )
        }
    }
}