package com.luke.mangamachinetask.presentation.manga_listings

sealed class MangaListingEvent {
    data object FetchMangas : MangaListingEvent()
    data class UpdateSorting(val sortOption: SortOption) : MangaListingEvent()
    data class MarkAsFavorite(val mangaId: String) : MangaListingEvent()
    data class ScrollToYear(val year: Int) : MangaListingEvent()

}