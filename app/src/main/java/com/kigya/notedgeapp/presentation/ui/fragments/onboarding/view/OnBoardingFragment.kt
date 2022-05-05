package com.kigya.notedgeapp.presentation.ui.fragments.onboarding.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kigya.notedgeapp.databinding.OnboardingActivityBinding
import com.kigya.notedgeapp.presentation.ui.fragments.onboarding.OnBoardingViewModel
import com.kigya.notedgeapp.presentation.ui.fragments.onboarding.customView.OnBoardingButton
import com.kigya.notedgeapp.utils.extensions.navigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.onboarding_view.view.*

@AndroidEntryPoint
class OnBoardingFragment : Fragment() {

    private var _binding: OnboardingActivityBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<OnBoardingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = OnboardingActivityBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        setObservers()
    }

    private fun setListeners() {
        binding.customView.setListener { button ->
            when (button) {
                OnBoardingButton.NEXT -> {
                    navigateToNextSlide()
                }
                OnBoardingButton.SKIP -> {
                    openNoteList()
                }
                OnBoardingButton.FINISH_ONBOARDING -> {
                    openNoteList()
                }
            }
        }
    }

    private fun openNoteList() {
        viewModel.setOnboardingDone(true)
        navigator().openNoteList(clearBackstack = true, addToBackStack = false)
    }

    private fun setObservers() = Unit

    private fun navigateToNextSlide() {
        val nextSlidePos: Int = binding.customView.slider?.currentItem?.plus(1) ?: 0
        binding.customView.slider?.setCurrentItem(nextSlidePos, true)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        private val TAG = "OnBoardingFragment"
    }
}
