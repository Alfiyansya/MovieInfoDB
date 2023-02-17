package com.alfiansyah.movieinfodb.data.model

import com.alfiansyah.movieinfodb.data.source.remote.response.VideoResponse

data class Video(
    val id: String,
    val iso6391 : String,
    val iso31661: String,
    val name: String,
    val size: VideoSize,
    val type: VideoType,
    val key: String,
    val site: String,
    val movie: Movie
) {
    companion object {
        fun from(videoResponse: VideoResponse, movie: Movie): Video {
            return Video(
                id = videoResponse.id,
                iso6391 = videoResponse.iso6391,
                iso31661 = videoResponse.iso31661,
                name = videoResponse.name,
                size = VideoSize.toVideoSize(videoResponse.size),
                type = VideoType.toVideoType(videoResponse.type),
                key = videoResponse.key,
                movie = movie,
                site = videoResponse.site
            )
        }
    }
}