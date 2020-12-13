package com.example.faircon.di

import android.app.Application
import androidx.room.Room
import com.example.faircon.api.auth.AuthService
import com.example.faircon.api.main.MainService
import com.example.faircon.persistence.AccountPropertiesDao
import com.example.faircon.util.Constants
import com.example.faircon.persistence.AppDatabase
import com.example.faircon.persistence.AppDatabase.Companion.DATABASE_NAME
import com.example.faircon.persistence.AuthTokenDao
import com.example.faircon.repository.auth.AuthRepository
import com.example.faircon.repository.auth.AuthRepositoryImpl
import com.example.faircon.repository.main.AccountRepository
import com.example.faircon.repository.main.AccountRepositoryImpl
import com.example.faircon.session.SessionManager
import com.example.faircon.util.MyPreferences
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

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit
            .create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideMainService(retrofit: Retrofit): MainService {
        return retrofit
            .create(MainService::class.java)
    }

    @Singleton
    @Provides
    fun provideAuthRepository(
        authTokenDao: AuthTokenDao,
        accountPropertiesDao: AccountPropertiesDao,
        authService: AuthService,
        myPreferences: MyPreferences
    ): AuthRepository {
        return AuthRepositoryImpl(
            authTokenDao,
            accountPropertiesDao,
            authService,
            myPreferences
        )
    }

    @Singleton
    @Provides
    fun provideAccountRepository(
        mainService: MainService,
        accountPropertiesDao: AccountPropertiesDao,
        sessionManager: SessionManager
    ): AccountRepository {
        return AccountRepositoryImpl(
            mainService,
            accountPropertiesDao,
            sessionManager
        )
    }

    @Singleton
    @Provides
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room
            .databaseBuilder(app, AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthTokenDao(db: AppDatabase): AuthTokenDao {
        return db.getAuthTokenDao()
    }

    @Singleton
    @Provides
    fun provideAccountPropertiesDao(db: AppDatabase): AccountPropertiesDao {
        return db.getAccountPropertiesDao()
    }

}