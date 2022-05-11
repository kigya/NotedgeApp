package com.kigya.notedgeapp.domain.usecase

import com.kigya.notedgeapp.domain.repository.NoteRepository
import javax.inject.Inject

class ChangeNotePositionUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(from: Long, to: Long){

    }

}