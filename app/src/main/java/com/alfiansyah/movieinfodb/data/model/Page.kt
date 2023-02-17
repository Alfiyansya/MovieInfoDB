package com.alfiansyah.movieinfodb.data.model

data class Page<T>(
    val page: Int,
    val totalPages: Int,
    val totalResults: Int,
    val content: List<T>
)