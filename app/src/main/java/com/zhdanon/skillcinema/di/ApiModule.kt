package com.zhdanon.skillcinema.di

import com.zhdanon.skillcinema.data.api.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.zhdanon.skillcinema.data.api.CinemaApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {
    @Provides
    fun provideCinemaApi(): CinemaApi {
        val baseUrl = "https://kinopoiskapiunofficial.tech/api/"
        val apiKey = "ffcd0204-2065-4214-b6ae-aa29f5fe4003"

        val interceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(apiKey))
            .addInterceptor(interceptor)
            .build()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
            .create(CinemaApi::class.java)
    }
}