package com.kigya.notedgeapp.application

import android.app.Application
import com.kigya.notedgeapp.repository.NoteRepository

class NotedgeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NoteRepository.initialize(this)
    }
}