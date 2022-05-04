package com.kigya.notedgeapp.presentation.ui.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.kigya.notedgeapp.R
import com.kigya.notedgeapp.presentation.ui.onboarding.entity.OnBoardingPage
import kotlinx.android.synthetic.main.onboarding_page_item.view.*

class OnBoardingPagerAdapter(private val onBoardingPageList:Array<OnBoardingPage> = OnBoardingPage.values())
    : RecyclerView.Adapter<PagerViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): PagerViewHolder {
        return LayoutInflater.from(parent.context).inflate(
            PagerViewHolder.LAYOUT, parent, false
        ).let { PagerViewHolder(it) }
    }

    override fun getItemCount() = onBoardingPageList.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(onBoardingPageList[position])
    }
}

class PagerViewHolder(private val root: View) : RecyclerView.ViewHolder(root) {
    fun bind(onBoardingPage: OnBoardingPage) {
       val res = root.context.resources
        root.light_title_onboarding?.text = res.getString(onBoardingPage.titleResource)
        root.text_content_onboarding?.text = res.getString(onBoardingPage.subTitleResource)
        root.weight_title_onboarding?.text = res.getString(onBoardingPage.descriptionResource)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.onboarding_page_item
    }
}