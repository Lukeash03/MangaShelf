package com.luke.mangamachinetask.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MangaResponse(
    val results: List<MangaDto>
)
data class MangaDto(
    val id: String,
    val image: String,
    val score: Double,
    val popularity: Int,
    val title: String,
    val publishedChapterDate: Long,
    val category: String
)
