package com.kigya.notedgeapp.presentation.ui.fragments

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.kigya.notedgeapp.R
import com.kigya.notedgeapp.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val animR: AnimatedVectorDrawableCompat? =
            AnimatedVectorDrawableCompat.create(view.context, R.drawable.anim_logo)
        animR?.let {
            binding.mainLogo.setImageDrawable(it)
        }
        (binding.mainLogo.drawable as Animatable?)?.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}