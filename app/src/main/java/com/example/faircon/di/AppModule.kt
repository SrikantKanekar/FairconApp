package com.example.faircon.di

import android.app.Application
import androidx.room.Room
import com.example.faircon.util.Constants
//import com.example.faircon.persistence.AppDatabase
//import com.example.faircon.persistence.AppDatabase.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

//    @Singleton
//    @Provides
//    fun provideAppDatabase(app: Application): AppDatabase {
//        return Room
//            .databaseBuilder(app, AppDatabase::class.java, DATABASE_NAME)
//            .fallbackToDestructiveMigration()
//            .build()
//    }
}