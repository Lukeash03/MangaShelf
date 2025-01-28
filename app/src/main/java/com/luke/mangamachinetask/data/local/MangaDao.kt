package com.luke.mangamachinetask.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MangaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMangas(
        mangas: List<MangaEntity>
    )

    @Query("DELETE FROM mangaentity")
    suspend fun clearMangas()

}