package com.kigya.notedgeapp.domain.usecase

import com.kigya.notedgeapp.data.model.Note
import com.kigya.notedgeapp.domain.repository.NoteRepository
import javax.inject.Inject

class UpdateNoteUseCase @Inject constructor(
    private val repository: NoteRepository
){
    suspend operator fun invoke(note: Note){
        repository.updateNote(note)
    }
}