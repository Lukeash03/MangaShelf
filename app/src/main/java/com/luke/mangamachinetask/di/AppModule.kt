package com.luke.mangamachinetask.di

import android.app.Application
import androidx.room.Room
import com.luke.mangamachinetask.data.local.MangaDatabase
import com.luke.mangamachinetask.data.remote.MangaApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMangaApi(): MangaApiService {
        return Retrofit.Builder()
            .baseUrl(MangaApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideMangaDatabase(app: Application): MangaDatabase {
        return Room.databaseBuilder(
            app,
            MangaDatabase::class.java,
            "mangadb.db"
        ).build()
    }


}