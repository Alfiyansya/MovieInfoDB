package com.alfiansyah.movieinfodb.data.model

enum class VideoType(val value: String) {
    TRAILER("Trailer"),
    TEASER("Teaser"),
    CLIP("Clip"),
    FEATURE("Featurette"),
    BEHIND_THE_SCENES("Behind the Scenes"),
    BLOOPERS("Bloopers"),
    UNKNOWN("Unknown");

    companion object {
        fun toVideoType(value: String): VideoType {
            var found = UNKNOWN
            for (type in values()) {
                if (type.value == value) {
                    found = type
                    break
                }
            }

            return found
        }
    }
}