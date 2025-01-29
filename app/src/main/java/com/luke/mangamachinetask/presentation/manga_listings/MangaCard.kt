package com.luke.mangamachinetask.presentation.manga_listings

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.luke.mangamachinetask.R
import com.luke.mangamachinetask.domain.model.Manga
import com.luke.mangamachinetask.ui.theme.MangaMachineTaskTheme

@Composable
fun MangaCard(manga: Manga, onFavoriteToggle: () -> Unit) {
    Log.i("MangaCard", "Image: ${manga.image}")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Fixed-size Image
            AsyncImage(
                model = "https://cdn.myanimelist.net/images/anime/1018/136667.jpg",
//                ImageRequest.Builder(LocalContext.current)
//                    .data(manga.image)
//                    .crossfade(true)
//                    .build(),
                contentDescription = manga.title,
                modifier = Modifier
                    .height(100.dp)
                    .width(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                placeholder = painterResource(id = R.drawable.loading_img),
                error = painterResource(id = R.drawable.manga)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Box to wrap text and favorite icon
            Box(
                modifier = Modifier
                    .weight(1f) // Takes up remaining space
                    .fillMaxWidth()
            ) {
                // Middle Column (Text)
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(manga.title, style = MaterialTheme.typography.titleSmall)
                    Text("Score: ${manga.score}", style = MaterialTheme.typography.bodyMedium)
                    Text("Popularity: ${manga.popularity}", style = MaterialTheme.typography.bodyMedium)
                    Text("Date: ${manga.publishedDate}", style = MaterialTheme.typography.bodyMedium)
                }

                // Favorite Icon (Positioned at the Top-Right)
                IconButton(
                    onClick = onFavoriteToggle,
                    modifier = Modifier
                        .align(Alignment.TopEnd) // Align to the top-right inside Box
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Favorite",
                        modifier = Modifier.size(24.dp)
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
                "Manga"
            )
        ) {

        }
    }
}