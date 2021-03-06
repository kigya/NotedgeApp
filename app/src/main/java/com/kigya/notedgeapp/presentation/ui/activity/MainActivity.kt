package com.kigya.notedgeapp.presentation.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.R.id
import com.google.android.material.snackbar.Snackbar
import com.kigya.notedgeapp.R
import com.kigya.notedgeapp.data.model.Note
import com.kigya.notedgeapp.data.model.observeEvent
import com.kigya.notedgeapp.databinding.ActivityMainBinding
import com.kigya.notedgeapp.presentation.ui.fragments.SplashFragment
import com.kigya.notedgeapp.presentation.ui.fragments.note_detail.view.NoteFragment
import com.kigya.notedgeapp.presentation.ui.fragments.note_list.view.NoteListFragment
import com.kigya.notedgeapp.presentation.ui.fragments.onboarding.view.OnBoardingFragment
import com.kigya.notedgeapp.presentation.ui.navigation.Navigator
import com.kigya.notedgeapp.presentation.ui.navigation.Notifier
import com.kigya.notedgeapp.presentation.ui.navigation.NotifierCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Navigator, Notifier {

    private lateinit var binding: ActivityMainBinding

    private val currentFragment: Fragment get() = supportFragmentManager.findFragmentById(R.id.main_container)!!

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

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
        viewModel.warpToFragment.observeEvent(this) { direction ->
            when (direction) {
                Directions.MainFragment -> openNoteList(
                    firstTime = false,
                    clearBackstack = true,
                    addToBackStack = false
                )
                Directions.Onboarding -> openOnboarding(
                    firstTime = false,
                    clearBackstack = true,
                    addToBackStack = false
                )
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

    override fun onNoteSelected(note: Note) {
        openFragment(NoteFragment.newInstance(note))
    }

    override fun openNoteList(
        firstTime: Boolean,
        addToBackStack: Boolean,
        clearBackstack: Boolean
    ) {
        openFragment(
            NoteListFragment.newInstance(),
            firstTime = firstTime,
            clearBackstack = clearBackstack,
            addToBackStack = addToBackStack
        )
    }

    override fun openOnboarding(
        firstTime: Boolean,
        addToBackStack: Boolean,
        clearBackstack: Boolean
    ) {
        openFragment(
            OnBoardingFragment(),
            firstTime = firstTime,
            clearBackstack = clearBackstack,
            addToBackStack = addToBackStack
        )
    }

    override fun addOnBackpressedCallBack(callback: OnBackPressedCallback){
        onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun showSnackbar(@StringRes message: Int, notifierCallback: NotifierCallback?) {
        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
        val view = snackbar.view

        snackbar.setBackgroundTint(ContextCompat.getColor(view.context, R.color.dark_slay_gray))
        snackbar.setTextColor(Color.WHITE)

        val sBarPar = view.findViewById<AppCompatTextView>(id.snackbar_text)
        sBarPar.textSize = 17f
        sBarPar.textAlignment = View.TEXT_ALIGNMENT_CENTER

        view.setOnClickListener {
            snackbar.dismiss()
        }
        snackbar.show()

    }

}
