package com.kigya.notedgeapp.extensions

import com.kigya.notedgeapp.data.model.Note
import java.util.*

fun List<Note>.findIndexById(id: UUID): Int = this.indexOfFirst {
    it.id == id
}