package com.luke.mangamachinetask.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MangaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMangas(mangas: List<MangaEntity>)

    @Query("DELETE FROM mangaentity")
    suspend fun clearMangas()

    @Query("SELECT * FROM mangaentity ORDER BY publishedChapterDate ASC")
    suspend fun getAllManga(): List<MangaEntity>

//    @Query("SELECT * FROM mangaentity ORDER BY publishedChapterDate ASC")
//    fun getAllManga(): Flow<List<MangaEntity>>  // Change from suspend List to Flow

    @Query("SELECT * FROM mangaentity WHERE id = :mangaId LIMIT 1")
    suspend fun getMangaById(mangaId: String): MangaEntity?

    @Query("SELECT isFavorite FROM mangaentity WHERE id = :mangaId LIMIT 1")
    suspend fun isFavorite(mangaId: String): Boolean

    @Query("UPDATE mangaentity SET isFavorite = :isFavorite WHERE id = :mangaId")
    suspend fun updateFavoriteStatus(mangaId: String, isFavorite: Boolean)

    @Query("SELECT isRead FROM mangaentity WHERE id = :mangaId LIMIT 1")
    suspend fun isRead(mangaId: String): Boolean

    @Query("UPDATE mangaentity SET isRead = :isRead WHERE id = :mangaId")
    suspend fun updateReadStatus(mangaId: String, isRead: Boolean)

}