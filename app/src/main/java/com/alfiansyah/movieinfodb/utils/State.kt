package com.alfiansyah.movieinfodb.utils

abstract class State<T> {
    class Loading<T> : State<T>()
    class DataFetched<T>(val data: T): State<T>()
    class EmptyDataFetched<T>: State<T>()
    class ErrorOccurred<T>(s: String) : State<T>()
}