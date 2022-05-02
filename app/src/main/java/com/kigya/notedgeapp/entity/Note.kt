package com.kigya.notedgeapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "notes")
class Note : Serializable {

    @PrimaryKey(autoGenerate = true)
    val id: UUID = UUID.randomUUID()

    @ColumnInfo(name = "title")
    var title: String? = null

    @ColumnInfo(name = "date_time")
    var dateTime: Date? = Date()

    @ColumnInfo(name = "note_text")
    var noteText: String? = null

    override fun toString(): String {
        return "$title : $dateTime"
    }
}
