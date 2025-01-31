package com.luke.mangamachinetask.presentation.manga_listings

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
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
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
@Destination(start = true)
fun MangaListingScreen(
    navigator: DestinationsNavigator,
    viewModel: MangaListingViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    val topAppBarColor = MaterialTheme.colorScheme.background
    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(color = topAppBarColor)
    }

    val state = viewModel.state
    val selectedSortOption by viewModel.selectedSortOption.collectAsState()

    Scaffold(
        floatingActionButton = {
            ExpandableFAB(viewModel, state)
        }
    ) { padding ->
        if (state.mangaList.isNotEmpty()) {
            val categories = state.mangaList
                .groupBy { it.publishedDate.substringAfterLast(" ").toInt() } // Group by year
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
                        }.also {
                            println("Category: $it")
                        }
                    )
                }

            var activeYear by remember { mutableIntStateOf(categories.firstOrNull()?.year ?: 0) }
            val (selectedTabIndex, setSelectedTabIndex, listState) = lazyListTabSync(categories.indices.toList())
            val coroutineScope = rememberCoroutineScope()

            val yearStartIndices = remember(categories) {
                mutableMapOf<String, Int>().apply {
                    var currentIndex = 0
                    for (category in categories) {
                        this[category.year.toString()] = currentIndex
                        currentIndex += category.mangaList.size + 1
                    }
                }
            }.also {
                println("YearStartIndices: $it")
            }

            Column(modifier = Modifier.padding(padding)) {

                if (selectedSortOption == SortOption.None && !state.showingFavorites) {
                    YearTabBar(
                        years = categories.map { it.year },
                        selectedTabIndex = selectedTabIndex,
                        onTabClicked = { index, year ->
                            Log.i("ListScreen", "$year : $index")
                            setSelectedTabIndex(index)
                            coroutineScope.launch {
                                val scrollIndex = yearStartIndices[year.toString()] ?: 0
                                listState.animateScrollToItem(scrollIndex)
                            }
                        }
                    )
                }

                MangaLazyList(
                    categories = categories,
                    listState = listState,
                    viewModel = viewModel,
                    navigator = navigator
                )
            }
        } else if (state.isLoading) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .wrapContentSize(Alignment.Center), // Center the progress indicator in the screen
//            ) {
//                CircularProgressIndicator(
//                    modifier = Modifier.size(50.dp), // Set size for the progress indicator
//                    color = MaterialTheme.colorScheme.primary, // Optional: Customize color
//                    strokeWidth = 4.dp // Optional: Customize stroke width
//                )
//            }
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
fun YearTabBar(
    years: List<Int>, // List of years
    selectedTabIndex: Int,
    onTabClicked: (index: Int, year: Int) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        edgePadding = 0.dp
    ) {
        years.forEachIndexed { index, year ->
            Tab(
                selected = index == selectedTabIndex,
                onClick = { onTabClicked(index, year) },
                text = { Text("$year") }
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
    val selectedSortOption by viewModel.selectedSortOption.collectAsState()

    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        categories.forEach { category ->
            if (selectedSortOption == SortOption.None) {
                // Add year as a header
                item {
                    Text(
                        text = "${category.year}",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(8.dp)
                    )
                }
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

@Composable
fun ExpandableFAB(viewModel: MangaListingViewModel, state: MangaListingState) {
    var isFavoriteListShowing by remember { mutableStateOf(state.showingFavorites) }
    var isSortingDialogVisible by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val items = listOf(
        MiniFabItems(
            if (viewModel.state.showingFavorites) Icons.Filled.Favorite else Icons.Default.Favorite,
            "Favorite",
            {
                viewModel.onEvent(MangaListingEvent.ToggleFavorites)
                isFavoriteListShowing = !isFavoriteListShowing
            },
            if (isFavoriteListShowing) Color.Red else MaterialTheme.colorScheme.primary
        ),
        MiniFabItems(
            Icons.Filled.Edit,
            "Sort",
            {
                isSortingDialogVisible = true
            },
            MaterialTheme.colorScheme.primary
        ),
        MiniFabItems(
            Icons.Default.Refresh,
            "Refresh",
            { viewModel.onEvent(MangaListingEvent.FetchMangas(true)) },
            MaterialTheme.colorScheme.primary
        )
    )

    Row {
        if (isSortingDialogVisible) {
            SortingDialog(onDismiss = { isSortingDialogVisible = false }) { option ->
                viewModel.onEvent(MangaListingEvent.UpdateSorting(option))
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it }) + expandVertically(),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it }) + shrinkVertically()
            ) {
                LazyColumn {
                    items(items.size) {
                        ItemUi(
                            icon = items[it].icon,
                            title = items[it].title,
                            onClick = items[it].onClick,
                            tintColor = items[it].tintColor
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
            val transition = updateTransition(targetState = expanded, label = "Transition")
            val rotation by transition.animateFloat(label = "Rotation") {
                if (it) 315f else 0f
            }
            FloatingActionButton(
                onClick = { expanded = !expanded },
                modifier = Modifier.rotate(rotation),
                containerColor = MaterialTheme.colorScheme.background
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Icon")
            }
        }
    }
}

@Composable
fun ItemUi(icon: ImageVector, title: String, onClick: () -> Unit, tintColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
    ) {
        FloatingActionButton(
            onClick = { onClick() },
            modifier = Modifier.size(45.dp), containerColor = MaterialTheme.colorScheme.background
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = tintColor
            )
        }
    }
}

data class MiniFabItems(
    val icon: ImageVector,
    val title: String,
    val onClick: () -> Unit,
    val tintColor: Color
)

@Composable
fun SortingDialog(
    onDismiss: () -> Unit,
    onSortOptionSelected: (SortOption) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Sort By") },
        text = {
            Column {
                SortOption.entries.forEach { option ->
                    TextButton(
                        onClick = {
                            onSortOptionSelected(option)
                            onDismiss()
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = option.name,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        }
    )
}
