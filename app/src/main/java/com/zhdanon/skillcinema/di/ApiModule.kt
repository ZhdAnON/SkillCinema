package com.zhdanon.skillcinema.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.zhdanon.skillcinema.data.api.CinemaApi

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {
    @Provides
    fun provideCinemaAPI(): CinemaApi {
        return CinemaApi.getInstance()
    }
}