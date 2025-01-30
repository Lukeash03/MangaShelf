package com.luke.mangamachinetask.presentation.manga_listings

import com.luke.mangamachinetask.domain.model.Manga

// Represents a single manga
data class MangaItem(
    val id: String,
    val title: String,
    val publishedDate: String
)

// Represents a group of manga belonging to a specific year
data class YearCategory(
    val year: String,
    val mangaList: List<Manga>
)
