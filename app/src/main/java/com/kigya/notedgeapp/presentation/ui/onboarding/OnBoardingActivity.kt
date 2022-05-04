package com.kigya.notedgeapp.presentation.ui.onboarding

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.kigya.notedgeapp.databinding.OnboardingActivityBinding
import com.kigya.notedgeapp.presentation.ui.splash.MainViewModel

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: OnboardingActivityBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value
            }
        }
        binding = OnboardingActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

}
