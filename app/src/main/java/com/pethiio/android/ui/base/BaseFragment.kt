package com.pethiio.android.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.pethiio.android.data.model.PethiioResponse
import com.pethiio.android.ui.main.view.MainActivity

abstract class BaseFragment : Fragment() {

    protected open var bottomNavigationViewVisibility = View.VISIBLE

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // get the reference of the parent activity and call the setBottomNavigationVisibility method.
        if (activity is MainActivity) {
            val mainActivity = activity as MainActivity
            mainActivity.setBottomNavigationVisibility(bottomNavigationViewVisibility)

            setUpViews()
        }
    }

    open fun setUpViews() {}

    override fun onResume() {
        super.onResume()
        if (activity is MainActivity) {
            val mainActivity = activity as MainActivity
            mainActivity.setBottomNavigationVisibility(bottomNavigationViewVisibility)
        }
    }

    fun getLocalizedString(key: String, pethiioResponse: List<PethiioResponse>?): String {

        pethiioResponse?.forEach {
            if (it.key == key)
                return it.value
        }
        return ""
    }


}