package com.zhdanon.skillcinema.di

import com.zhdanon.skillcinema.data.RepositoryApi
import com.zhdanon.skillcinema.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindRepository(repositoryApi: RepositoryApi) : Repository
}