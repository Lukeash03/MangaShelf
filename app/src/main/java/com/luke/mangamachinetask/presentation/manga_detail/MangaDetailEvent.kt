package com.luke.mangamachinetask.presentation.manga_detail

sealed class MangaDetailEvent {
    data class ToggleFavorite(val mangaId: String) : MangaDetailEvent()
    data class MarkAsRead(val mangaId: String) : MangaDetailEvent()
}