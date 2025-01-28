package com.luke.mangamachinetask.data.remote.dto

data class MangaDto(
    val id: String,
    val image: String,
    val score: Double,
    val popularity: Int,
    val title: String,
    val publishedChapterDate: Long,
    val category: String
)
