package com.luke.mangamachinetask.data.repository

import com.luke.mangamachinetask.data.local.MangaDao
import com.luke.mangamachinetask.data.mapper.toEntity
import com.luke.mangamachinetask.data.mapper.toDomain
import com.luke.mangamachinetask.data.remote.MangaApiService
import com.luke.mangamachinetask.domain.model.Manga
import com.luke.mangamachinetask.domain.repository.MangaRepository
import com.luke.mangamachinetask.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MangaRepositoryImpl(
    private val apiService: MangaApiService,
    private val mangaDao: MangaDao
): MangaRepository {

    override fun getMangaList(): Flow<Resource<List<Manga>>> = flow {
        emit(Resource.Loading())

        try {
            val apiResponse = apiService.getMangaList()
            val mangaEntities = apiResponse.map { it.toEntity() }

            mangaDao.insertMangas(mangaEntities)

            val domainMangaList = mangaDao.getAllManga().map { it.toDomain() }
            emit(Resource.Success(domainMangaList))
        } catch (e: Exception) {
            val cachedManga = mangaDao.getAllManga().map { it.toDomain() }
            if (cachedManga.isNotEmpty()) {
                emit(Resource.Error("Network error. Showing cached data.", cachedManga))
            } else {
                emit(Resource.Error("Network error. No cached data available"))
            }
        }
    }
}