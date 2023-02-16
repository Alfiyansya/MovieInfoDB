package com.alfiansyah.movieinfodb.di

import com.alfiansyah.movieinfodb.data.source.repo.AllRepository
import com.alfiansyah.movieinfodb.data.source.repo.AllRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideRepository(allRepository: AllRepositoryImpl): AllRepository

}