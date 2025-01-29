package com.luke.mangamachinetask.presentation.manga_detail

import com.luke.mangamachinetask.domain.model.Manga

data class MangaDetailState(
    val manga: Manga? = null,
    val isFavorite: Boolean = false,
    val isRead: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
