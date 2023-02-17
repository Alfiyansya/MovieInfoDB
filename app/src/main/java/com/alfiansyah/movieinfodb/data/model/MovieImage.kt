package com.alfiansyah.movieinfodb.data.model

import com.alfiansyah.movieinfodb.BuildConfig
import com.alfiansyah.movieinfodb.data.source.remote.response.MovieDetailResponse
import com.alfiansyah.movieinfodb.data.source.remote.response.MovieResponse


data class MovieImage(
    var backDropPath: String?,
    var posterPath: String?
) {
    companion object{
        fun from(movie: MovieResponse): MovieImage {
            return MovieImage(
                backDropPath = "${BuildConfig.TMDB_IMAGE_BASE_URL}${movie.backDropPath}",
                posterPath = "${BuildConfig.TMDB_IMAGE_BASE_URL}${movie.posterPath}"
            )
        }

        fun from(movieItem: MovieDetailResponse): MovieImage? {
            return if (movieItem.backdropPath == null && movieItem.posterPath == null) null
            else MovieImage(
                    backDropPath = "${BuildConfig.TMDB_IMAGE_BASE_URL}${movieItem.backdropPath}",
                    posterPath = "${BuildConfig.TMDB_IMAGE_BASE_URL}${movieItem.posterPath}"
            )
        }
    }
}