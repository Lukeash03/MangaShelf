package com.luke.mangamachinetask.data.mapper

import com.luke.mangamachinetask.data.local.MangaEntity
import com.luke.mangamachinetask.domain.model.Manga
import java.text.SimpleDateFormat
import java.util.Locale

fun MangaEntity.toManga(): Manga {
    val dateFormatter = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    val formattedDate = dateFormatter.format(publishedChapterDate)

    return Manga(
        id = id,
        image = image,
        score = score,
        popularity = popularity,
        title = title,
        publishedDate = formattedDate,
        category = category
    )
}

fun Manga.toEntity(): MangaEntity {
    val dateFormatter = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    val timestamp = dateFormatter.parse(publishedDate)?.time ?: 0L

    return MangaEntity(
        id = id,
        image = image,
        score = score,
        popularity = popularity,
        title = title,
        publishedChapterDate = timestamp,
        category = category
    )
}
