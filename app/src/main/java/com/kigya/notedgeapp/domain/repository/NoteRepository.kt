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
    suspend fun setTo0(target: Long)
    suspend fun increasePosValue(from: Long, to: Long)
    suspend fun decreasePosValue(from: Long, to: Long)
    suspend fun setWhere0(target: Long)

}