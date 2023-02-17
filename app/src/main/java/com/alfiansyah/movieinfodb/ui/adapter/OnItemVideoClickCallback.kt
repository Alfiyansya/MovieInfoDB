package com.alfiansyah.movieinfodb.ui.adapter

import com.alfiansyah.movieinfodb.data.model.Video

interface OnItemVideoClickCallback {
    fun onItemClicked(video: Video)
}