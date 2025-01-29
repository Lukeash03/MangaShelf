package com.luke.mangamachinetask.data.repository

import android.util.Log
import com.luke.mangamachinetask.data.local.MangaDatabase
import com.luke.mangamachinetask.data.mapper.toEntity
import com.luke.mangamachinetask.data.mapper.toDomain
import com.luke.mangamachinetask.data.remote.MangaApiService
import com.luke.mangamachinetask.domain.model.Manga
import com.luke.mangamachinetask.domain.repository.MangaRepository
import com.luke.mangamachinetask.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MangaRepositoryImpl @Inject constructor(
    private val apiService: MangaApiService,
    private val mangaDB: MangaDatabase
): MangaRepository {

    private val dao = mangaDB.dao

    override suspend fun getMangaList(): Flow<Resource<List<Manga>>> = flow {
        emit(Resource.Loading())

        try {
            val apiResponse = apiService.getMangaList()
            Log.i("MangaRepo", "ApiResponse: $apiResponse")
            val mangaEntities = apiResponse.map { it.toEntity() }

            dao.insertMangas(mangaEntities)

            val domainMangaList = dao.getAllManga().map { it.toDomain() }
            emit(Resource.Success(domainMangaList))
        } catch (e: Exception) {
            Log.i("MangaRepo", "ApiResponse error: $e")
            val cachedManga = dao.getAllManga().map { it.toDomain() }
            if (cachedManga.isNotEmpty()) {
                emit(Resource.Error("Network error. Showing cached data.", cachedManga))
            } else {
                emit(Resource.Error("Network error. No cached data available"))
            }
        }
    }

    override suspend fun getMangaById(mangaId: String): Resource<Manga> {
        return try {
            val manga = dao.getMangaById(mangaId)
            if (manga != null) {
                Resource.Success(manga.toDomain())
            } else {
                Resource.Error("Manga not found")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error("Error fetching Manga.")
        }
    }

    override suspend fun isFavorite(mangaId: String): Boolean {
        return dao.isFavorite(mangaId)
    }

    override suspend fun setFavoriteStatus(mangaId: String, isFavorite: Boolean) {
        dao.updateFavoriteStatus(mangaId, isFavorite)
    }

    override suspend fun isRead(mangaId: String): Boolean {
        return dao.isRead(mangaId)
    }

    override suspend fun setReadStatus(mangaId: String, isRead: Boolean) {
        dao.updateReadStatus(mangaId, isRead)
    }
}