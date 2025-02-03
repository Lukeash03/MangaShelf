package com.luke.mangamachinetask.presentation.manga_listings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.luke.mangamachinetask.presentation.manga_listings.MangaListingViewModel
import com.luke.mangamachinetask.presentation.manga_listings.YearCategory
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun MangaLazyList(
    categories: List<YearCategory>, // Manga grouped by year
    listState: LazyListState = rememberLazyListState(),
    viewModel: MangaListingViewModel,
    navigator: DestinationsNavigator
) {
    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(categories, key = { index, category -> category.year }) { _, category ->
            MangaCategory(
                category = category,
                viewModel = viewModel,
                navigator = navigator
            )
        }
    }
}
