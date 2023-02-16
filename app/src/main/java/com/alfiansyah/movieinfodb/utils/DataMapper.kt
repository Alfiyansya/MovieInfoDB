package com.alfiansyah.movieinfodb.utils

import com.alfiansyah.movieinfodb.data.entity.Genre
import com.alfiansyah.movieinfodb.data.source.remote.response.GenreResponse

object DataMapper {
    fun mapGenreResponsesToModel(input: List<GenreResponse>): List<Genre> {
        val movieList = ArrayList<Genre>()
        input.map {
            val tourism = Genre(
                id = it.id,
                name = it.name
            )
            movieList.add(tourism)
        }
        return movieList
    }
}