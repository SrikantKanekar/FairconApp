package com.example.faircon.framework.di

import com.example.faircon.framework.datasource.network.mappers.FairconMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {

    @Provides
    fun provideFairconMapper() = FairconMapper()
}