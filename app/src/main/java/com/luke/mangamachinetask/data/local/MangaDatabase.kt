package com.luke.mangamachinetask.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MangaEntity::class],
    version = 1
)
abstract class MangaDatabase: RoomDatabase() {
    abstract val dao: MangaDao
}