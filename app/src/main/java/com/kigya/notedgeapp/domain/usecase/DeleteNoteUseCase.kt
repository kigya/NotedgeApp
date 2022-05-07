package com.kigya.notedgeapp.domain.usecase

import com.kigya.notedgeapp.domain.repository.NoteRepository
import java.util.*
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: UUID){
        repository.deleteNote(id)
    }
}