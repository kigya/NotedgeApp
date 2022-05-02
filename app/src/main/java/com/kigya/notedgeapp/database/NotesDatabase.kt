package com.kigya.notedgeapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kigya.notedgeapp.Note
import com.kigya.notedgeapp.converters.NoteTypeConverters
import com.kigya.notedgeapp.dao.NoteDao

@Database(
    entities = [Note::class],
    version = 1
)
@TypeConverters(NoteTypeConverters::class)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun crimeDao(): NoteDao

}
