package com.kigya.notedgeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import java.util.*

class MainActivity : AppCompatActivity(), NoteListFragment.Callbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fm: FragmentManager = supportFragmentManager
        val currentFragment = fm.findFragmentById(R.id.container)

        if (currentFragment == null) {
            val fragment = NoteListFragment.newInstance()
            fm.beginTransaction()
                .add(R.id.container, fragment)
                .commit()
        }
    }

    override fun onNoteSelected(noteId: UUID) {
        val fragment = NoteFragment.newInstance(noteId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
