package com.kigya.notedgeapp.domain.repository

import android.webkit.PermissionRequest
import com.kigya.notedgeapp.data.model.Note
import kotlinx.coroutines.flow.Flow
import java.util.*

interface NoteRepository {

    fun getNotes(): Flow<List<Note>>
    suspend fun getNote(id: Long): Note?
    suspend fun addNote(note: Note)
    suspend fun deleteNote(id: Long)
    suspend fun updateNote(note: Note)
    fun search(request: String?): Flow<List<Note>>

}