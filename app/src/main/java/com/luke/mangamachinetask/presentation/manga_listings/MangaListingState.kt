package com.luke.mangamachinetask.presentation.manga_listings

import com.luke.mangamachinetask.domain.model.Manga

data class MangaListingState(
    val mangaList: List<Manga> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedSortOption: SortOption = SortOption.None,
    val showingFavorites: Boolean = false
)
