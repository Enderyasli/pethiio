package com.pethiio.android.ui.main.view.onboarding

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.pethiio.android.R

class OnboardingViewPagerAdapter(
    manager: FragmentManager,
    private val context: Context
) :
    FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    // Returns total number of pages
    override fun getCount(): Int {
        return NUM_ITEMS
    }



    // Returns the fragment to display for that page
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> OnBoardingInsideFragment.newInstance(
                context.resources.getString(R.string.title_onboarding_1),
                R.drawable.onboard1
            )
            1 -> OnBoardingInsideFragment.newInstance(
                context.resources.getString(R.string.title_onboarding_2),
                R.drawable.onboard2
            )
            2 -> OnBoardingInsideFragment.newInstance(
                context.resources.getString(R.string.title_onboarding_3),
                R.drawable.onboard3,
            )
            else -> null
        }!!
    }

    // Returns the page title for the top indicator
    override fun getPageTitle(position: Int): CharSequence? {
        return "Page $position"
    }

    companion object {
        private const val NUM_ITEMS = 3
    }

}