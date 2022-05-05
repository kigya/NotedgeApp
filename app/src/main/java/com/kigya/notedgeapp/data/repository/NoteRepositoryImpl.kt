package com.kigya.notedgeapp.data.repository

import android.util.Log
import com.kigya.notedgeapp.data.local.room.dao.NoteDao
import com.kigya.notedgeapp.data.model.Note
import com.kigya.notedgeapp.domain.repository.NoteRepository
import java.util.*
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) : NoteRepository {

    override suspend fun getNotes(): List<Note> = noteDao.getNotes()

    override suspend fun getNote(id: UUID): Note? = noteDao.getNote(id)

    override suspend fun addNote(note: Note) {
        noteDao.addNote(note)
    }

    override suspend fun deleteNote(id: UUID) {
        noteDao.deleteSpecificNote(id)
    }

    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
        Log.d("NoteRepository", "${note.noteText} ${note.title}")
    }

}