package com.luke.mangamachinetask.presentation.manga_listings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luke.mangamachinetask.presentation.destinations.MangaDetailScreenDestination
import com.luke.mangamachinetask.presentation.manga_listings.MangaListingEvent
import com.luke.mangamachinetask.presentation.manga_listings.MangaListingViewModel
import com.luke.mangamachinetask.presentation.manga_listings.SortOption
import com.luke.mangamachinetask.presentation.manga_listings.YearCategory
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun ItemCategory(
    category: YearCategory,
    viewModel: MangaListingViewModel,
    navigator: DestinationsNavigator
) {
    val selectedSortOption by viewModel.selectedSortOption.collectAsState()
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        if (selectedSortOption == SortOption.None) {
            Text(category.year, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            category.mangaList.forEach {
                MangaCard(
                    manga = it,
                    onFavoriteToggle = { viewModel.onEvent(MangaListingEvent.MarkAsFavorite(it.id)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navigator.navigate(
                                MangaDetailScreenDestination(it.id)
                            )
                        }
                )
            }
        }
    }
}