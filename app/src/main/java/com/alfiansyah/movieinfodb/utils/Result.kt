package com.alfiansyah.movieinfodb.utils

sealed class Result<T> {
    data class Success<T>(val data: T): Result<T>()
    data class Failed<T>(val throwable: Throwable): Result<T>()
}