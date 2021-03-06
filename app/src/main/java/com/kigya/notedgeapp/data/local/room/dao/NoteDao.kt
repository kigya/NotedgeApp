package com.kigya.notedgeapp.data.local.room.dao

import androidx.room.*
import com.kigya.notedgeapp.data.model.Note
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes")
    fun getNotes(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE id=(:id)")
    suspend fun getNote(id: UUID): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note: Note)

    @Query("DELETE FROM notes WHERE id =(:id)")
    suspend fun deleteSpecificNote(id: UUID)

    @Update(entity = Note::class)
    suspend fun updateNote(note: Note)

    @Query("SELECT * FROM notes WHERE title LIKE :request OR note_text LIKE :request")
    fun searchNotes(request: String?): Flow<List<Note>>

}
