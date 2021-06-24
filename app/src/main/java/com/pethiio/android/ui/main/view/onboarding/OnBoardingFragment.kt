package com.pethiio.android.ui.main.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.pethiio.android.R
import com.pethiio.android.ui.base.BaseFragment
import com.pethiio.android.utils.PreferenceHelper
import kotlinx.android.synthetic.main.fragment_on_boarding.*

class OnBoardingFragment : BaseFragment() {

    private lateinit var title: String
    private var imageResource = 0

    private lateinit var mViewPager: ViewPager
    private lateinit var textSkip: TextView
    private lateinit var textNextStep: TextView

    override var bottomNavigationViewVisibility = View.GONE


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_on_boarding, container, false)
        mViewPager = view.findViewById(R.id.viewPager) as ViewPager
        viewPager
        mViewPager.adapter = OnboardingViewPagerAdapter(childFragmentManager, requireContext())
        mViewPager.offscreenPageLimit = 1
//
        textSkip = view.findViewById(R.id.text_skip) as TextView
        textSkip.setOnClickListener {
            PreferenceHelper.SharedPreferencesManager.getInstance().isFirstDownload = false
            findNavController().navigate(R.id.action_navigation_onboarding_to_navigation_welcome)
        }
        textNextStep = view.findViewById(R.id.txt_next_step) as TextView

        textNextStep.setOnClickListener {
            if (getItem(+1) > mViewPager.childCount - 1) {
                findNavController().navigate(R.id.action_navigation_start_to_navigation_main)
            } else {
                mViewPager.setCurrentItem(getItem(+1), true)
            }
        }
        return view
    }

    private fun getItem(i: Int): Int {
        return mViewPager.currentItem + i
    }


}