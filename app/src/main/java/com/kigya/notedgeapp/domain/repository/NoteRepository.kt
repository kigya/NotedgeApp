package com.kigya.notedgeapp.domain.repository

import android.webkit.PermissionRequest
import com.kigya.notedgeapp.data.model.Note
import kotlinx.coroutines.flow.Flow
import java.util.*

interface NoteRepository {

    fun getNotes(): Flow<List<Note>>
    suspend fun getNote(id: UUID): Note?
    suspend fun addNote(note: Note)
    suspend fun deleteNote(id: UUID)
    suspend fun updateNote(note: Note)
    fun search(request: String?): Flow<List<Note>>

}