package com.kigya.notedgeapp.presentation.ui.fragments.onboarding.customView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.viewpager2.widget.ViewPager2
import com.kigya.notedgeapp.R
import com.kigya.notedgeapp.core.setParallaxTransformation
import com.kigya.notedgeapp.data.model.OnBoardingPage
import com.kigya.notedgeapp.presentation.common.OnBoardingPagerAdapter
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import kotlinx.android.synthetic.main.onboarding_view.view.*

enum class OnBoardingButton {
    SKIP, NEXT, FINISH_ONBOARDING
}

typealias OnBoardingActionListener = (OnBoardingButton) -> Unit

class OnBoardingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var listener: OnBoardingActionListener? = null

    private val numberOfPages by lazy { OnBoardingPage.values().size }

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.onboarding_view, this, true)
        setUpSlider(view)
        initializeListeners()
    }

    private fun setUpSlider(view: View) {
        with(slider) {
            adapter = OnBoardingPagerAdapter()
            setPageTransformer { page, position ->
                setParallaxTransformation(page, position)
            }

            addSlideChangeListener()

            val wormDotsIndicator = view.findViewById<WormDotsIndicator>(R.id.page_indicator)
            wormDotsIndicator.setViewPager2(this)
        }
    }

    private fun addSlideChangeListener() {

        slider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (numberOfPages > 1) {
                    val newProgress = (position + positionOffset) / (numberOfPages - 1)
                    onboardingRoot.progress = newProgress
                }
            }
        })
    }

    fun setListener(listener: OnBoardingActionListener?) {
        this.listener = listener
    }

    private fun initializeListeners() {
        nextBtn.setOnClickListener {
            this.listener?.invoke(OnBoardingButton.NEXT)
        }
        skipBtn.setOnClickListener {
            this.listener?.invoke(OnBoardingButton.SKIP)
        }
        startBtn.setOnClickListener {
            this.listener?.invoke(OnBoardingButton.FINISH_ONBOARDING)
        }
    }

}