package com.luke.mangamachinetask.data.mapper

import com.luke.mangamachinetask.data.local.MangaEntity
import com.luke.mangamachinetask.data.remote.dto.MangaDto
import com.luke.mangamachinetask.domain.model.Manga
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Date
import java.util.Locale

fun MangaDto.toEntity(): MangaEntity {
    return MangaEntity(
        id = id,
        image = image,
        score = score,
        popularity = popularity,
        title = title,
        publishedChapterDate = publishedChapterDate,
        category = category
    )
}

fun MangaEntity.toDomain(): Manga {
    return Manga(
        id = id,
        image = image,
        score = score,
        popularity = popularity,
        title = title,
        publishedDate = formatEpochToDate(publishedChapterDate),
        category = category,
        isFavorite = isFavorite
    )
}

fun formatEpochToDate(epoch: Long, format: String = "MMMM yyyy"): String {
    val date = Date(epoch * 1000) // Convert seconds to milliseconds
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    return formatter.format(date)
}


fun Long.toYear(): Int = Instant.ofEpochSecond(this)
    .atZone(ZoneId.systemDefault())
    .year