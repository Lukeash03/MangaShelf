package com.luke.mangamachinetask.presentation.manga_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luke.mangamachinetask.domain.repository.MangaRepository
import com.luke.mangamachinetask.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MangaDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: MangaRepository
): ViewModel() {

    var state by mutableStateOf(MangaDetailState())
        private set

    fun onEvent(event: MangaDetailEvent) {
        when (event) {
            is MangaDetailEvent.LoadMangaDetails -> {

            }
            MangaDetailEvent.MarkAsRead -> {

            }
            MangaDetailEvent.ToggleFavorite -> {

            }
        }
    }

    private fun loadMangaDetails(mangaId: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            when (val result = repository.getMangaById(mangaId)) {
                is Resource.Error -> {
                    state = state.copy(
                        error = result.message ?: "An unexpected error occurred.",
                        isLoading = false
                    )
                }
                is Resource.Loading -> {
                    // Already handled
                }
                is Resource.Success -> {
                    val manga = result.data
                    val isFavorite = repository.isFavorite(mangaId)
                    val isRead = repository.isRead(mangaId)
                    state = state.copy(
                        manga = manga,
                        isFavorite = isFavorite,
                        isRead = isRead,
                        isLoading = false
                    )
                }
            }


        }
    }

}