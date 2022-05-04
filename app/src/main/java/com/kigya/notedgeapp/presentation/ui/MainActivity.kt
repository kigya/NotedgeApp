package com.kigya.notedgeapp.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.kigya.notedgeapp.R
import com.kigya.notedgeapp.presentation.ui.note_detail.view.NoteFragment
import com.kigya.notedgeapp.presentation.ui.note_list.view.NoteListFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Navigator {
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
