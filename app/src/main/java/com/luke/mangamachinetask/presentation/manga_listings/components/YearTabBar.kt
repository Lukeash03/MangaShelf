package com.luke.mangamachinetask.presentation.manga_listings.components

import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.luke.mangamachinetask.presentation.manga_listings.YearCategory

@Composable
fun YearTabBar(
    yearCategories: List<YearCategory>, // List of years
    selectedTabIndex: Int,
    onTabClicked: (index: Int, yearCategory: YearCategory) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        edgePadding = 0.dp
    ) {
        yearCategories.forEachIndexed { index, category ->
            Tab(
                selected = index == selectedTabIndex,
                onClick = { onTabClicked(index, category) },
                text = { Text(category.year) }
            )
        }
    }
}
