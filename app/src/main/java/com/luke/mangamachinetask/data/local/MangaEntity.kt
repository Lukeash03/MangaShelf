package com.luke.mangamachinetask.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MangaEntity(
    @PrimaryKey val id: String,
    val image: String,
    val score: Double,
    val popularity: Int,
    val title: String,
    val publishedChapterDate: Long,
    val category: String

)
