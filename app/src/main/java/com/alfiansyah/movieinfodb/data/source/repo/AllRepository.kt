package com.alfiansyah.movieinfodb.data.source.repo

import com.alfiansyah.movieinfodb.data.entity.Genre
import com.alfiansyah.movieinfodb.utils.Result

interface AllRepository {
    suspend fun getGenres(): Result<List<Genre>>

}