package com.kigya.notedgeapp.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kigya.notedgeapp.Note
import java.util.*

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes")
    fun getNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM notes WHERE id=(:id)")
    fun getNote(id: UUID): LiveData<Note?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNote(note: Note)

    @Query("DELETE FROM notes WHERE id =(:id)")
    fun deleteSpecificNote(id: UUID)

    @Update
    fun updateNote(note: Note)

}
