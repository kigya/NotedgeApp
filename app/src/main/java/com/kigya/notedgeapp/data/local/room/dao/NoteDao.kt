package com.kigya.notedgeapp.data.local.room.dao

import androidx.room.*
import com.kigya.notedgeapp.data.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes")
    fun getNotes(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE id=(:id)")
    suspend fun getNote(id: Long): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note: Note)

    @Query("DELETE FROM notes WHERE id =(:id)")
    suspend fun deleteSpecificNote(id: Long)

    @Update(entity = Note::class)
    suspend fun updateNote(note: Note)

    @Query("SELECT * FROM notes WHERE title LIKE :request OR note_text LIKE :request")
    fun searchNotes(request: String?): Flow<List<Note>>

    @Query("UPDATE notes SET position = 0 WHERE position =:target")
    suspend fun setTo0(target: Long)

    @Query("UPDATE notes SET position = position + 1 WHERE position BETWEEN :from AND :to - 1")
    suspend fun increasePosValue(from: Long, to: Long)

    @Query("UPDATE notes SET position = position - 1 WHERE position BETWEEN :from + 1 AND :to")
    suspend fun decreasePosValue(from: Long, to: Long)

    @Query("UPDATE notes SET position =:target WHERE position = 0")
    suspend fun setWhere0(target: Long)

}
