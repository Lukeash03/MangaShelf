package com.luke.mangamachinetask.domain.repository

import com.luke.mangamachinetask.domain.model.Manga
import com.luke.mangamachinetask.util.Resource
import kotlinx.coroutines.flow.Flow

interface MangaRepository {
    fun getMangaList(): Flow<Resource<List<Manga>>>
    // Fetch all mangas from the local database
}