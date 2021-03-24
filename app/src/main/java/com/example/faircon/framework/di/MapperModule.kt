package com.example.faircon.framework.di

import com.example.faircon.framework.datasource.network.mappers.ParameterMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {

    @Provides
    fun provideParameterMapper() = ParameterMapper()
}