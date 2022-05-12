package com.kigya.notedgeapp.data.repository

import android.util.Log
import com.kigya.notedgeapp.data.local.room.dao.NoteDao
import com.kigya.notedgeapp.data.model.Note
import com.kigya.notedgeapp.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) : NoteRepository {

    override  fun getNotes(): Flow<List<Note>> = noteDao.getNotes()

    override suspend fun getNote(id: Long): Note? = noteDao.getNote(id)

    override suspend fun addNote(note: Note) {
        noteDao.addNote(note)
    }

    override suspend fun deleteNote(id: Long) {
        noteDao.deleteSpecificNote(id)
    }

    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }

    override fun search(request: String?): Flow<List<Note>> {
        return noteDao.searchNotes(request)
    }

    override suspend fun setTo0(target: Long) {
        noteDao.setTo0(target)
    }

    override suspend fun increasePosValue(from: Long, to: Long) {
        noteDao.increasePosValue(from,to)
    }

    override suspend fun decreasePosValue(from: Long, to: Long) {
        noteDao.decreasePosValue(from,to)
    }

    override suspend fun setWhere0(target: Long) {
        noteDao.setWhere0(target)
    }

}