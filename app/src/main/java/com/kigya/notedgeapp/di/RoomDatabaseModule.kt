package com.kigya.notedgeapp.di

import android.app.Application
import androidx.room.Room
import com.kigya.notedgeapp.data.local.room.NotesDatabase
import com.kigya.notedgeapp.data.local.room.dao.NoteDao
import com.kigya.notedgeapp.data.repository.NoteRepositoryImpl
import com.kigya.notedgeapp.domain.repository.NoteRepository
import com.kigya.notedgeapp.utils.constants.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDatabaseModule {

    @Provides
    @Singleton
    fun providesNotedgeBD(application: Application): NotesDatabase {
        return Room.databaseBuilder(
            application,
            NotesDatabase::class.java,
            Constants.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun providesNoteDao(database: NotesDatabase): NoteDao = database.noteDao()

    @Provides
    @Singleton
    fun providesRepository(noteDao: NoteDao): NoteRepository = NoteRepositoryImpl(noteDao)

}