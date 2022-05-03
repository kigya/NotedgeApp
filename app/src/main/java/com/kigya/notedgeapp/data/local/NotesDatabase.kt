package com.kigya.notedgeapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kigya.notedgeapp.data.model.Note
import com.kigya.notedgeapp.data.local.dao.NoteDao

@Database(entities = [Note::class], version = 1, exportSchema = false)
@TypeConverters(NoteTypeConverters::class)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

}
