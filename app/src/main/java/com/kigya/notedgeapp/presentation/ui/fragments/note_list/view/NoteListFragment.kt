package com.kigya.notedgeapp.presentation.ui.fragments.note_list.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.transition.MaterialFadeThrough
import com.kigya.notedgeapp.R
import com.kigya.notedgeapp.data.model.Note
import com.kigya.notedgeapp.data.model.observeEvent
import com.kigya.notedgeapp.databinding.FragmentHomeBinding
import com.kigya.notedgeapp.presentation.common.NotesRecyclerAdapter
import com.kigya.notedgeapp.presentation.ui.fragments.note_detail.EventsNotificationContract
import com.kigya.notedgeapp.presentation.ui.fragments.note_list.viewmodel.NotesListViewModel
import com.kigya.notedgeapp.utils.extensions.navigator
import com.kigya.notedgeapp.utils.extensions.notifier
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteListFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: NotesRecyclerAdapter

    private val noteListViewModel by viewModels<NotesListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentTransitions()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        adapter = NotesRecyclerAdapter(noteListViewModel)
        binding.recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycle.addObserver(noteListViewModel)


        initializeListeners()
        initializeObservers()
    }

    private fun initializeListeners() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query != null) {
                    search(query)
                }
                return true
            }
        })
        binding.searchView.setOnCloseListener {

            return@setOnCloseListener true
        }
        binding.root.setOnClickListener {
            with(binding.searchView) {
                setQuery("", false)
                clearFocus()
            }
        }
        binding.createNoteButton.setOnClickListener {
            navigator().onNoteSelected(Note())
        }
    }

    private fun initializeObservers() {

        noteListViewModel.noteListLiveData().observe(viewLifecycleOwner) { notes ->
            adapter.notes = notes.toMutableList()
        }
        noteListViewModel.onItemSelected.observeEvent(viewLifecycleOwner) { note ->
            navigator().onNoteSelected(note)
        }

        noteListViewModel.notificationLD.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                EventsNotificationContract.SAVED -> {
                    notifier().showSnackbar(R.string.note_saved)
                }
                EventsNotificationContract.DELETED -> {
                    notifier().showSnackbar(R.string.removed)
                }
            }
        }
    }

    private fun search(request: String) {
        val query = "%$request%"
        noteListViewModel.search(query).observe(viewLifecycleOwner) { list ->
            list.let {
                adapter.notes = it.toMutableList()
            }
        }
    }

    private fun setFragmentTransitions() {
        exitTransition = MaterialFadeThrough()
        enterTransition = MaterialFadeThrough()
        returnTransition = MaterialFadeThrough()
        reenterTransition = MaterialFadeThrough()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): NoteListFragment {
            return NoteListFragment()
        }

        @JvmStatic
        private val TAG = "NoteListFragment"

    }

}