package com.luke.mangamachinetask.presentation.manga_listings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.luke.mangamachinetask.presentation.destinations.MangaDetailScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination(start = true)
fun MangaListingScreen(
//    onEvent: (MangaListingEvent) -> Unit,
    navigator: DestinationsNavigator,
    viewModel: MangaListingViewModel = hiltViewModel()
) {
    val state = viewModel.state
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manga Shelf") },
                actions = {
                    // Sort Dropdown
                    SortDropdown(selectedOption = state.selectedSortOption) { option ->
                        viewModel.onEvent(MangaListingEvent.UpdateSorting(option))
                    }
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh icon")
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Year Tabs
//            if (state.selectedSortOption == SortOption.NONE) {
//                ScrollableTabRow(
//                    selectedTabIndex = 0, // Replace with current year tab index logic
//                    edgePadding = 8.dp
//                ) {
//                    state.mangaList.groupBy { it.publishedDate.substringAfterLast(" ") }.keys.forEach { year ->
//                        Tab(
//                            selected = false, // Replace with logic to check if this is the selected tab
//                            onClick = { viewModel.onEvent(MangaListingEvent.ScrollToYear(year.toInt())) }
//                        ) {
//                            Text(year)
//                        }
//                    }
//                }
//            }

            // Manga List
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.mangaList) { manga ->
                    MangaCard(
                        manga = manga,
                        onFavoriteToggle = { viewModel.onEvent(MangaListingEvent.MarkAsFavorite(manga.id)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navigator.navigate(
                                    MangaDetailScreenDestination(manga.id)
                                )
                            }
                    )
                }
            }
        }

        state.errorMessage?.let {
            // Show Error Message
            Snackbar(
                action = {
                    TextButton(onClick = { viewModel.onEvent(MangaListingEvent.FetchMangas) }) {
                        Text("Retry")
                    }
                }
            ) { Text(it) }
        }
    }
}

@Composable
fun SortDropdown(
    selectedOption: SortOption,
    onSortOptionSelected: (SortOption) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Filled.Edit, contentDescription = "Sort")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            SortOption.values().forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(option.name) // Displaying the enum value name
                    },
                    onClick = {
                        expanded = false
                        onSortOptionSelected(option)
                    }
                )
            }
        }
    }
}
