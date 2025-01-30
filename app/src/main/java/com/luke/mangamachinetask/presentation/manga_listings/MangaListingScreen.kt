package com.luke.mangamachinetask.presentation.manga_listings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.ahmadhamwi.tabsync_compose.lazyListTabSync
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.luke.mangamachinetask.domain.model.Manga
import com.luke.mangamachinetask.presentation.destinations.MangaDetailScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination(start = true)
fun MangaListingScreen(
    navigator: DestinationsNavigator,
    viewModel: MangaListingViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    val topAppBarColor = MaterialTheme.colorScheme.onPrimaryContainer
    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(color = topAppBarColor)
    }

    val state = viewModel.state
    val lifecycleOwner = LocalLifecycleOwner.current

//    DisposableEffect(lifecycleOwner) {
//        val observer = LifecycleEventObserver { _, event ->
//            if (event == Lifecycle.Event.ON_RESUME ) {
//                viewModel.onEvent(MangaListingEvent.FetchMangas(false))
//            }
//        }
//        lifecycleOwner.lifecycle.addObserver(observer)
//        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
//    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manga Shelf") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = topAppBarColor,
                    actionIconContentColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.background
                ),
                actions = {
                    IconButton(onClick = { viewModel.onEvent(MangaListingEvent.ToggleFavorites) }) {
                        Icon(
                            imageVector = if (state.showingFavorites) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Toggle Favorites",
                            tint = if (state.showingFavorites) Color.Red else Color.Gray
                        )
                    }
                    // Sort Dropdown
                    SortDropdown(selectedOption = state.selectedSortOption) { option ->
                        viewModel.onEvent(MangaListingEvent.UpdateSorting(option))
                    }
                    IconButton(onClick = {
                        viewModel.onEvent(MangaListingEvent.FetchMangas(true))
                    }) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh icon")
                    }
                }
            )
        }
    ) { padding ->

        if (state.mangaList.isNotEmpty()) {
            val categories = state.mangaList
                .groupBy { it.publishedDate.substringAfterLast(" ") } // Group by year
                .map { (year, mangas) ->
                    YearCategory(
                        year = year,
                        mangaList = mangas.map { manga ->
                            Manga(
                                id = manga.id,
                                title = manga.title,
                                publishedDate = manga.publishedDate,
                                image = manga.image,
                                category = manga.category,
                                isFavorite = manga.isFavorite,
                                isRead = manga.isRead,
                                popularity = manga.popularity,
                                score = manga.score
                            )
                        }
                    )
                }

            val (selectedTabIndex, setSelectedTabIndex, listState) = lazyListTabSync(categories.indices.toList())

            Column(modifier = Modifier.padding(padding)) {

                YearTabBar(
                    years = categories.map { it.year },
                    selectedTabIndex = selectedTabIndex,
                    onTabClicked = { index, _ -> setSelectedTabIndex(index) }
                )

                MangaLazyList(
                    categories = categories,
                    viewModel = viewModel,
                    navigator = navigator
                )
            }
        } else if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
//                CircularProgressIndicator()
            }
        }

        state.errorMessage?.let {
            // Show Error Message
            Snackbar(
                action = {
                    TextButton(onClick = { viewModel.onEvent(MangaListingEvent.FetchMangas(true)) }) {
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

@Composable
fun YearTabBar(
    years: List<String>, // List of years
    selectedTabIndex: Int,
    onTabClicked: (index: Int, year: String) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        edgePadding = 0.dp
    ) {
        years.forEachIndexed { index, year ->
            Tab(
                selected = index == selectedTabIndex,
                onClick = { onTabClicked(index, year) },
                text = { Text(year) }
            )
        }
    }
}

@Composable
fun MangaLazyList(
    categories: List<YearCategory>, // Manga grouped by year
    listState: LazyListState = rememberLazyListState(),
    viewModel: MangaListingViewModel,
    navigator: DestinationsNavigator
) {
    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        categories.forEach { category ->
            // Add year as a header
            item {
                Text(
                    text = category.year,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(8.dp)
                )
            }

            // Add manga cards under the year
            items(category.mangaList) { manga ->
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
}
