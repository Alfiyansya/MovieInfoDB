package com.alfiansyah.movieinfodb.di

import com.alfiansyah.movieinfodb.data.source.remote.api.ApiBuilder
import com.alfiansyah.movieinfodb.data.source.remote.api.NetworkService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideNetworking(): NetworkService {
        return ApiBuilder.createService()
    }
}