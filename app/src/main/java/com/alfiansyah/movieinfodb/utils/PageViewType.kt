package com.alfiansyah.movieinfodb.utils

enum class PageViewType(val value: Int) {
    DATA(1),
    NO_MORE_DATA(2),
    ERROR_OCCURED(3),
    LOADING(4);

    companion object {

        fun toPageViewType(type: Int): PageViewType {
            var found = NO_MORE_DATA
            for (pageViewType in values()) {
                if (pageViewType.value == type) {
                    found = pageViewType
                    break
                }
            }

            return found
        }
    }
}