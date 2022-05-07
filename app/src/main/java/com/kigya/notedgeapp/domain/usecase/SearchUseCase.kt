package com.kigya.notedgeapp.domain.usecase

import com.kigya.notedgeapp.data.model.Note
import com.kigya.notedgeapp.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    operator fun invoke(request: String?): Flow<List<Note>> {
        return repository.search(request)
    }
}