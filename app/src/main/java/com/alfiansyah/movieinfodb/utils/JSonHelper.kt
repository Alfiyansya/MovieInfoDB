package com.alfiansyah.movieinfodb.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object JSonHelper {
    private val gson: Gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd HH:mm:ss")
        .disableHtmlEscaping()
        .create()

    fun <T> fromJson(kClass: Class<T>, strJson: String): T {
        return gson.fromJson(strJson, kClass)
    }
}