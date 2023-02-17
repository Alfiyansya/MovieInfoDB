package com.alfiansyah.movieinfodb.data.model

import com.alfiansyah.movieinfodb.data.source.remote.response.MovieDetailResponse
import com.alfiansyah.movieinfodb.data.source.remote.response.MovieResponse
import com.alfiansyah.movieinfodb.utils.DateFormatterHelper
import com.alfiansyah.movieinfodb.utils.MapperList
import java.math.BigInteger
import java.util.*

data class Movie(
    val id: BigInteger,
    val title: String?,
    val overview: String?,
    var movieImage: MovieImage?,
    val genres: List<Genre>,
    var releaseDate: Date,
    var vote: Vote
) {
    companion object {


        fun from(movieItem: MovieResponse): Movie {
            val image = MovieImage.from(movieItem)
            val vote = Vote.from(movieItem)
            val genres = MapperList.map(movieItem.genreIds) {
                Genre.from(it)
            }

            return Movie(
                id = movieItem.id,
                genres = genres,
                movieImage = image,
                overview = movieItem.overview,
                releaseDate = DateFormatterHelper.toDateInstance(movieItem.releaseDate),
                title = movieItem.title,
                vote = vote
            )
        }

        fun from(movieItem: MovieDetailResponse): Movie {
            val image = MovieImage.from(movieItem)
            val vote = Vote.from(movieItem)
            val genres = MapperList.map(movieItem.genres) {
                Genre.from(it)
            }


            return Movie(
                id = movieItem.id,
                genres = genres,
                movieImage = image,
                overview = movieItem.overview ?: "",
                releaseDate = DateFormatterHelper.toDateInstance(movieItem.releaseDate),
                title = movieItem.title,
                vote = vote
            )
        }

    }
}