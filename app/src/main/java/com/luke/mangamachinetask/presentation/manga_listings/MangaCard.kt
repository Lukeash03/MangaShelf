package com.luke.mangamachinetask.presentation.manga_listings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.luke.mangamachinetask.domain.model.Manga
import com.luke.mangamachinetask.ui.theme.MangaMachineTaskTheme

@OptIn(ExperimentalCoilApi::class)
@Composable
fun MangaCard(
    manga: Manga,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Fixed-size Image
            Image(
                painter = rememberImagePainter(data = manga.image),
                contentDescription = manga.title,
                modifier = Modifier
                    .weight(1f)
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp)),
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Box to wrap text and favorite icon
            Box(
                modifier = Modifier
                    .weight(2f) // Takes up remaining space
                    .fillMaxSize()
            ) {
                // Middle Column (Text)
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        manga.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text("Score: ${manga.score}", style = MaterialTheme.typography.bodyMedium)
                    Text("Popularity: ${manga.popularity}", style = MaterialTheme.typography.bodyMedium)
                    Text("Published year: ${manga.publishedDate.substringAfterLast(" ")}", style = MaterialTheme.typography.bodyMedium)
                }

                // Favorite Icon (Positioned at the Top-Right)
                IconButton(
                    onClick = onFavoriteToggle,
                    modifier = Modifier
                        .align(Alignment.BottomEnd) // Align to the top-right inside Box
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = if (manga.isFavorite) Icons.Filled.Favorite else Icons.Default.Favorite,
                        contentDescription = "Favorite",
                        modifier = Modifier.size(24.dp),
                        tint = if (manga.isFavorite) Color.Red else Color.Gray
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun MangaCardPreview() {
    MangaMachineTaskTheme {
        MangaCard(
            manga = Manga(
                "4e70e91ac092255ef70016d6",
                "https://cdn.myanimelist.net/images/anime/6/73245.jpg",
                15.3,
                1234,
                "Neon Genesis Evangelion: Shinji Ikari Raising Project",
                "2010",
                "Manga",
                true
            ),
            modifier = Modifier,
            onFavoriteToggle = {}
        )
    }
}