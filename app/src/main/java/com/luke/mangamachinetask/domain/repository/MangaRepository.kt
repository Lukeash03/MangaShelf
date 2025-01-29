package com.luke.mangamachinetask.domain.repository

import com.luke.mangamachinetask.domain.model.Manga
import com.luke.mangamachinetask.presentation.manga_listings.MangaListingEvent
import com.luke.mangamachinetask.util.Resource
import kotlinx.coroutines.flow.Flow

interface MangaRepository {
    suspend fun getMangaList(): Flow<Resource<List<Manga>>>
    // Fetch all mangas from the local database

    suspend fun getMangaById(mangaId: String): Resource<Manga>

    suspend fun isFavorite(mangaId: String): Boolean

    suspend fun setFavoriteStatus(mangaId: String, isFavorite: Boolean)

    suspend fun isRead(mangaId: String): Boolean

    suspend fun setReadStatus(mangaId: String, isRead: Boolean)

}