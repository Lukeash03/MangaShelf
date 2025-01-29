package com.luke.mangamachinetask.presentation.manga_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.luke.mangamachinetask.domain.model.Manga
import com.luke.mangamachinetask.ui.theme.MangaMachineTaskTheme
import com.ramcosta.composedestinations.annotation.Destination

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoilApi::class)
@Composable
@Destination
fun MangaDetailScreen(
    mangaId: String,
    viewModel: MangaDetailViewModel = hiltViewModel()
) {

    val state = viewModel.state
    if (state.error == null) {

        Scaffold(
            topBar = {
                TopAppBar(title = { Text(text = "Manga Details") })
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                if (state.isLoading) {
//                    CircularProgressIndicator()
                } else {
                    state.manga?.let { manga ->
                        Card(
                            modifier = Modifier
//                                .fillMaxSize()
                                .padding(16.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(8.dp)
                        ) {
                            Column(
                                modifier = Modifier
//                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // Cover Image
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = rememberImagePainter(data = manga.image),
                                        contentDescription = "Cover Image",
                                        modifier = Modifier
                                            .height(250.dp)
                                            .clip(RoundedCornerShape(16.dp)),
                                        contentScale = ContentScale.Crop,
                                    )

                                }

                                // Title
                                Text(
                                    text = manga.title,
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )

                                // Score and Popularity
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Score: ${manga.score}", fontSize = 16.sp)
                                    Text(text = "Popularity: ${manga.popularity}", fontSize = 16.sp)
                                }

                                // Published Date
                                Text(text = "Published: ${manga.publishedDate}", fontSize = 16.sp)

                                // Category
                                Text(text = "Category: ${manga.category}", fontSize = 16.sp)

                                // Favorite and Read Buttons
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Button(onClick = {
                                        viewModel.onEvent(MangaDetailEvent.ToggleFavorite(mangaId))
                                    }) {
                                        Text(
                                            text = if (state.isFavorite) "Unfavorite" else "Favorite"
                                        )
                                    }

                                    Button(
                                        onClick = {
                                            viewModel.onEvent(MangaDetailEvent.MarkAsRead(mangaId))
                                        },
//                                        enabled = !state.isRead
                                    ) {
                                        Text(text = if (state.isRead) "Read" else "Mark as Read")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun MangaDetailContent(
    manga: Manga,
    onEvent: (MangaDetailEvent) -> Unit
) {
    Card(
        modifier = Modifier
//            .fillMaxSize()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
//                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Cover Image
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberImagePainter(data = manga.image),
                    contentDescription = "Cover Image",
                    modifier = Modifier
                        .height(250.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop,
                )

            }

            // Title
            Text(
                text = manga.title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            // Score and Popularity
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Score: ${manga.score}", fontSize = 16.sp)
                Text(text = "Popularity: ${manga.popularity}", fontSize = 16.sp)
            }

            // Published Date
            Text(text = "Published: ${manga.publishedDate}", fontSize = 16.sp)

            // Category
            Text(text = "Category: ${manga.category}", fontSize = 16.sp)

            // Favorite and Read Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
//                                    onEvent(MangaDetailEvent.ToggleFavorite)
                }) {
                    Text(
                        text = if (manga.isFavorite) "Unfavorite" else "Favorite"
                    )
                }

                Button(
                    onClick = {
//                                        onEvent(MangaDetailEvent.MarkAsRead)
                    },
                    enabled = !manga.isRead
                ) {
                    Text(text = if (manga.isRead) "Read" else "Mark as Read")
                }
            }
        }
    }
}

@Preview
@Composable
fun MangaDetailCardPreview() {
    MangaMachineTaskTheme {
        MangaDetailContent(
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