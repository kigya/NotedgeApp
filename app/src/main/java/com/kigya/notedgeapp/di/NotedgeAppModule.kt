package com.kigya.notedgeapp.di

import android.content.Context
import com.kigya.notedgeapp.data.preferences.NoteAppPreferencesImpl
import com.kigya.notedgeapp.domain.preferences.NoteAppPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotedgeAppModule {

    @Singleton
    @Provides
    fun providePreferences(@ApplicationContext context: Context): NoteAppPreferences {
        return NoteAppPreferencesImpl.getDefaultPreferenceInstance(context)
    }

}