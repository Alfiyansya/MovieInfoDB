package com.alfiansyah.movieinfodb.data.source.repo

import com.alfiansyah.movieinfodb.data.model.*
import com.alfiansyah.movieinfodb.utils.Result
import java.math.BigInteger

interface AllRepository {
    suspend fun getGenres(): Result<List<Genre>>
    suspend fun getPageMovieByGenre(genre: Genre,pageNumber:Int): Result<Page<Movie>>
    suspend fun getDetailMovieById(movieId : BigInteger): Result<Movie>
    suspend fun getReviewsMovie(movie: Movie, pageNumber: Int): Result<Page<Review>>
    suspend fun getVideosMovie(movie: Movie): Result<List<Video>>
}