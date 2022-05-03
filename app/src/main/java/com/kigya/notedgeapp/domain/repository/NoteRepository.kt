package com.kigya.notedgeapp.domain.repository

import com.kigya.notedgeapp.data.model.Note
import java.util.*

interface NoteRepository {

    suspend fun getNotes(): List<Note>
    suspend fun getNote(id: UUID): Note?
    suspend fun addNote(note: Note)
    suspend fun deleteNote(id: UUID)
    suspend fun updateNote(note: Note)

}