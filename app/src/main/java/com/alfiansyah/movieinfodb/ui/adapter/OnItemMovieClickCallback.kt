package com.alfiansyah.movieinfodb.ui.adapter

import com.alfiansyah.movieinfodb.data.model.Movie

interface OnItemMovieClickCallback {
    fun onItemClicked(movie: Movie )
}