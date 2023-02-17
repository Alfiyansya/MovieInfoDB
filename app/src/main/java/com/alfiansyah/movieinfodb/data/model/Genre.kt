package com.alfiansyah.movieinfodb.data.model

import android.os.Parcelable
import com.alfiansyah.movieinfodb.data.source.remote.response.GenreResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class Genre(
    var id: Int,
    var name: String
): Parcelable{
    companion object {
        fun from(genreId: Int): Genre {
            return Genre(
                id = genreId,
                name = ""
            )
        }

        fun from(genre: GenreResponse): Genre {
            return Genre(
                id = genre.id,
                name = genre.name
            )
        }
    }
}