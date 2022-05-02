package com.kigya.notedgeapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "notes")
class Note : Serializable {

    @PrimaryKey
    var id: UUID = UUID.randomUUID()

    @ColumnInfo(name = "title")
    var title: String = ""

    @ColumnInfo(name = "date_time")
    var dateTime: Date = Date()

    @ColumnInfo(name = "note_text")
    var noteText: String = ""

    init {
        id = UUID.randomUUID()
        title = ""
        dateTime = Date()
        noteText = ""
    }
    override fun toString(): String {
        return "$title : $dateTime"
    }
}
