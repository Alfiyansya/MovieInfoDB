package com.alfiansyah.movieinfodb.utils

object MapperList {
    fun <I, O> map(inputs: List<I>, map: (input: I) -> O): List<O> {
        val list = arrayListOf<O>()
        inputs.forEach {
            val output = map(it)
            list.add(output)
        }
        return list
    }
}