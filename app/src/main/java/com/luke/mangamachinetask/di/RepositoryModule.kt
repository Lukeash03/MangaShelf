package com.luke.mangamachinetask.di

import com.luke.mangamachinetask.data.repository.MangaRepositoryImpl
import com.luke.mangamachinetask.domain.repository.MangaRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMangaRepository(
        mangaRepositoryImpl: MangaRepositoryImpl
    ): MangaRepository

}