package com.kigya.notedgeapp.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(tableName = "notes")
@Parcelize
data class Note(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "title")
    var title: String = "",
    @ColumnInfo(name = "date_time")
    var dateTime: Date = Date(),
    @ColumnInfo(name = "note_text")
    var noteText: String = "",
) : Parcelable
