package com.kigya.notedgeapp.domain.usecase

import com.kigya.notedgeapp.domain.repository.NoteRepository
import javax.inject.Inject

class ChangeNotePositionUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(from: Long, to: Long) {
        when {
            from < to -> {                                      /*UPDATE notes SET position = 0 WHERE position = 1*/
                repository.setTo0(from)                           /*UPDATE notes SET position = position - 1 WHERE position BETWEEN 2 AND 6*/
                repository.decreasePosValue(from, to)       /*UPDATE notes SET position = 6 WHERE position = 0*/
                repository.setWhere0(to)
            }

            from > to -> {                                      /*UPDATE notes SET position = 0 WHERE position = 6*/
                repository.setTo0(from)                         /*UPDATE notes SET position = position + 1 WHERE position BETWEEN 1 AND 5*/
                repository.increasePosValue(to, from)       /*UPDATE notes SET position = 1 WHERE position = 0*/
                repository.setWhere0(to)
            }
            from == to -> {
                return
            }
        }
    }

}