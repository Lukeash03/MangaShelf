package com.luke.mangamachinetask.domain.model

data class Manga(
    val id: String,
    val image: String,
    val score: Double,
    val popularity: Int,
    val title: String,
    val publishedDate: String, // Formatted for presentation (e.g., "June 2021")
    val category: String
)
