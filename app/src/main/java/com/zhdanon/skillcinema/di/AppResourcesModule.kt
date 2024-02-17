package com.zhdanon.skillcinema.di

import android.content.Context
import com.zhdanon.skillcinema.core.AppResources
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppResourcesModule {
    @Provides
    fun provideResource(@ApplicationContext context: Context): AppResources {
        return AppResources(context)
    }
}