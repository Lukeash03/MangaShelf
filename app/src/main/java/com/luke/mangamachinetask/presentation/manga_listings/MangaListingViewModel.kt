package com.luke.mangamachinetask.presentation.manga_listings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luke.mangamachinetask.domain.model.Manga
import com.luke.mangamachinetask.domain.repository.MangaRepository
import com.luke.mangamachinetask.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MangaListingViewModel @Inject constructor(
    private val repository: MangaRepository
) : ViewModel() {

    var state by mutableStateOf(MangaListingState())
        private set

    init {
        fetchMangaList()
    }

    fun onEvent(event: MangaListingEvent) {
        when (event) {
            is MangaListingEvent.FetchMangas -> fetchMangaList(true)
            is MangaListingEvent.UpdateSorting -> updateSorting(event.sortOption)
            is MangaListingEvent.MarkAsFavorite -> toggleFavoriteStatus(event.mangaId)
            is MangaListingEvent.ScrollToYear -> {
//                scrollToYear(event.year)
            }
            MangaListingEvent.ToggleFavorites -> toggleFavorites()
        }
    }

    private fun fetchMangaList(fetchFromRemote: Boolean = false) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            repository.getMangaList(fetchFromRemote).collect { resource ->
                state = when (resource) {
                    is Resource.Success -> {
                        val sortedList =
                            applySorting(resource.data ?: emptyList(), state.selectedSortOption)
                        state.copy(mangaList = sortedList, isLoading = false, errorMessage = null)
                    }

                    is Resource.Error -> {
                        state.copy(isLoading = false, errorMessage = resource.message)
                    }

                    is Resource.Loading -> {
                        state.copy(isLoading = true)
                    }
                }
            }
        }
    }

    private fun updateSorting(sortOption: SortOption) {
        val sortedList = applySorting(state.mangaList, sortOption)
        state = state.copy(mangaList = sortedList, selectedSortOption = sortOption)
    }

    private fun applySorting(mangaList: List<Manga>, option: SortOption): List<Manga> {
        return when (option) {
            SortOption.ScoreAscending -> mangaList.sortedBy { it.score }
            SortOption.ScoreDescending -> mangaList.sortedByDescending { it.score }
            SortOption.PopularityAscending -> mangaList.sortedBy { it.popularity }
            SortOption.PopularityDescending -> mangaList.sortedByDescending { it.popularity }
            else -> mangaList
        }
    }

    private fun toggleFavoriteStatus(mangaId: String) {
        viewModelScope.launch {
            val updatedList = state.mangaList.map { manga ->
                if (manga.id == mangaId) {
                    val newFavoriteStatus = !manga.isFavorite
                    // Persist favorite status in the repository
                    repository.setFavoriteStatus(mangaId, newFavoriteStatus)
                    manga.copy(isFavorite = newFavoriteStatus)
                } else {
                    manga
                }
            }

            // Update the state to reflect the new favorite status
            state = state.copy(mangaList = updatedList)
        }
    }

    private fun toggleFavorites() {
        viewModelScope.launch {
            val showingFavorites = !state.showingFavorites // Toggle the current state
            state = state.copy(showingFavorites = showingFavorites)

            if (showingFavorites) {
                // Fetch and show only favorite mangas
                repository.getFavoriteMangas().collect { resource ->
                    state = when (resource) {
                        is Resource.Success -> {
                            val sortedList =
                                applySorting(resource.data ?: emptyList(), state.selectedSortOption)
                            state.copy(mangaList = sortedList, isLoading = false, errorMessage = null)
                        }

                        is Resource.Error -> {
                            state.copy(isLoading = false, errorMessage = resource.message)
                        }

                        is Resource.Loading -> {
                            state.copy(isLoading = true)
                        }
                    }
                }
            } else {
                // Show all mangas
                fetchMangaList()
            }
        }
    }
}