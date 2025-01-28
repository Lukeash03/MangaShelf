package com.luke.mangamachinetask.data.remote

import com.luke.mangamachinetask.data.remote.dto.MangaDto
import retrofit2.http.GET

interface MangaApiService {

    @GET("/")
    suspend fun getMangaList(): List<MangaDto>

    companion object {
        const val BASE_URL = "https://www.jsonkeeper.com/b/KEJO"
    }

}