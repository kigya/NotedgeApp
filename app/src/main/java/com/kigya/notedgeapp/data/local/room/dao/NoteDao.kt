package com.kigya.notedgeapp.data.local.room.dao

import androidx.room.*
import com.google.android.material.circularreveal.CircularRevealHelper
import com.kigya.notedgeapp.data.model.Note
import java.util.*

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes")
    suspend fun getNotes(): List<Note>

    @Query("SELECT * FROM notes WHERE id=(:id)")
    suspend fun getNote(id: UUID): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note: Note)

    @Query("DELETE FROM notes WHERE id =(:id)")
    suspend fun deleteSpecificNote(id: UUID)

    @Update(entity = Note::class)
    suspend fun updateNote(note: Note)

}
