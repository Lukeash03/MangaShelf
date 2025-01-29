package com.luke.mangamachinetask.presentation.manga_detail

sealed class MangaDetailEvent {
    data object ToggleFavorite: MangaDetailEvent()
    data object MarkAsRead: MangaDetailEvent()
    data class LoadMangaDetails(val mangaId: String): MangaDetailEvent()
}