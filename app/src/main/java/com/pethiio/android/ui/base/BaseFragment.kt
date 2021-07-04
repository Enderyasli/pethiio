package com.pethiio.android.ui.base

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.fragment.app.Fragment
import com.pethiio.android.data.model.LookUpsResponse
import com.pethiio.android.data.model.PethiioResponse
import com.pethiio.android.ui.main.view.MainActivity

abstract class BaseFragment : Fragment() {

    protected open var bottomNavigationViewVisibility = View.VISIBLE
    private var lookUpsResponse: List<LookUpsResponse>? = null


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

    fun getLocalizedSpan(key: String, pethiioResponse: List<PethiioResponse>?): SpannableString? {

        pethiioResponse?.forEach {
            if (it.key == key)
                return getSpannableString(it.value)
        }
        return null

    }

    private fun getSpannableString(key: String): SpannableString {

        val spannable = SpannableString("$key *")
        spannable.setSpan(
            ForegroundColorSpan(Color.RED),
            key.length,// start
            key.length + 2, // end
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        return spannable
    }

    fun getLookUps(key: String, lookUpsResponse: List<LookUpsResponse>?): List<String> {

        lookUpsResponse?.forEach { it ->
            if (it.key == key) {

                val arrayList = arrayListOf<String>()
                it.value.forEach {
                    arrayList.add(it.value)
                }

                return arrayList

            }
        }
        return emptyList()
    }

    fun setLookUps(lookUpsResponseList: List<LookUpsResponse>) {
        lookUpsResponse = lookUpsResponseList
    }

    fun getLookUpKey(key: String, value: String): String {

        lookUpsResponse?.forEach { it ->
            if (it.key == key) {

                it.value.forEach {
                    if (it.value == value)
                        return it.key
                }
            }
        }
        return ""
    }


}