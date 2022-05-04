package com.kigya.notedgeapp.data.model
import androidx.annotation.StringRes
import com.kigya.notedgeapp.R

enum class OnBoardingPage(
    @StringRes val titleResource: Int,
    @StringRes val subTitleResource: Int,
    @StringRes val descriptionResource: Int,
) {

    ONE(R.string.onboarding_slide1_title, R.string.onboarding_slide1_subtitle,R.string.onboarding_slide1_desc),
    TWO(R.string.onboarding_slide2_title, R.string.onboarding_slide2_subtitle,R.string.onboarding_slide2_desc),
    THREE(R.string.onboarding_slide3_title, R.string.onboarding_slide3_subtitle,R.string.onboarding_slide3_desc)

}