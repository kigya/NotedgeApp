package com.kigya.notedgeapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.kigya.notedgeapp.Note
import com.kigya.notedgeapp.database.NotesDatabase
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "notes-database"

class NoteRepository private constructor(context: Context) {

    private val database: NotesDatabase = Room.databaseBuilder(
        context.applicationContext,
        NotesDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val noteDao = database.crimeDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getNotes(): LiveData<List<Note>> = noteDao.getNotes()

    fun getNote(id: UUID): LiveData<Note?> = noteDao.getNote(id)

    fun addNote(note: Note) {
        executor.execute {
            noteDao.addNote(note)
        }
    }

    fun deleteNote(id: UUID) {
        executor.execute {
            noteDao.deleteSpecificNote(id)
        }
    }

    fun updateNote(note: Note) {
        executor.execute {
            noteDao.updateNote(note)
        }
    }

    companion object {
        private var INSTANCE: NoteRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = NoteRepository(context)
            }
        }

        fun get(): NoteRepository {
            return INSTANCE ?: throw IllegalStateException("NoteRepository must be initialized")
        }
    }

}