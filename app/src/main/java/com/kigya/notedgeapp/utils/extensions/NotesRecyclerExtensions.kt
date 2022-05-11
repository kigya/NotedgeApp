package com.kigya.notedgeapp.utils.extensions

import com.kigya.notedgeapp.data.model.Note
import java.util.*

fun List<Note>.findIndexById(id: Long): Int = this.indexOfFirst {
    it.id == id
}