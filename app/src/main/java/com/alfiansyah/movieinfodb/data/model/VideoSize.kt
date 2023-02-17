package com.alfiansyah.movieinfodb.data.model

enum class VideoSize(val value: Int) {
    S_360(360),
    S_480(480),
    S_720(720),
    S_1080(1080),
    UNKNOWN(0);

    companion object {
        fun toVideoSize(size: Int): VideoSize {
            var found = UNKNOWN
            for (videoSize in values()) {
                if (videoSize.value == size) {
                    found = videoSize
                    break
                }
            }

            return found
        }
    }
}