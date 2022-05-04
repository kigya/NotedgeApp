package com.kigya.notedgeapp.presentation.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.kigya.notedgeapp.R
import com.kigya.notedgeapp.data.model.observeEvent
import com.kigya.notedgeapp.presentation.ui.fragments.SplashFragment
import com.kigya.notedgeapp.presentation.ui.fragments.note_list.view.NoteListFragment
import com.kigya.notedgeapp.presentation.ui.fragments.onboarding.view.OnBoardingFragment
import com.kigya.notedgeapp.presentation.ui.navigation.Navigator
import com.kigya.notedgeapp.presentation.ui.note_detail.view.NoteFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Navigator {

    private val currentFragment: Fragment get() = supportFragmentManager.findFragmentById(R.id.main_container)!!

    private val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setObservers()

        appStatusCheckAndStart(savedInstanceState)
    }

    private fun appStatusCheckAndStart(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            openFragment(SplashFragment(), clearBackstack = true, addToBackStack = false)
            viewModel.defineIfFirstTime()
        }
    }

    private fun setObservers() {
        viewModel.warpToFragment.observeEvent(this) { dir ->
            when (dir) {
                Directions.MainFragment -> openNoteList(true)
                Directions.Onboarding -> openFragment(OnBoardingFragment(), firstTime = false, clearBackstack = true, addToBackStack = false)
            }
        }
    }

    private fun openFragment(
        fragment: Fragment,
        firstTime: Boolean = false,
        addToBackStack: Boolean = true,
        clearBackstack: Boolean = false
    ) {
        if (clearBackstack) {
            clearBackStack()
        }
        when {
            firstTime -> {
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.main_container, fragment)
                    .commit()
            }
            addToBackStack -> {
                supportFragmentManager
                    .beginTransaction()
                    .addToBackStack(fragment.javaClass.name)
                    .replace(R.id.main_container, fragment, fragment.javaClass.name)
                    .commit()
            }
            else -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, fragment, fragment.javaClass.name)
                    .commit()
            }
        }
    }

    private fun clearBackStack() =
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

    override fun onNoteSelected(noteId: UUID) {
        openFragment(NoteFragment.newInstance(noteId))
    }

    override fun openNoteList(clearBackstack: Boolean, addToBackStack: Boolean) {
        openFragment(NoteListFragment.newInstance(), clearBackstack, addToBackStack)
    }

    override fun openOnboarding(clearBackstack: Boolean, addToBackStack: Boolean) {
        openFragment(OnBoardingFragment(), clearBackstack, addToBackStack)
    }

}
