package com.alfiansyah.movieinfodb.ui.adapter

import com.alfiansyah.movieinfodb.data.model.Genre

interface OnItemGenreClickCallback {
    fun onItemClicked(genre : Genre)
}