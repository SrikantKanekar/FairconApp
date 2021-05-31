package com.example.faircon.di

import com.example.faircon.framework.datasource.network.mappers.FairconMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {

    @Singleton
    @Provides
    fun provideFairconMapper() = FairconMapper()
}