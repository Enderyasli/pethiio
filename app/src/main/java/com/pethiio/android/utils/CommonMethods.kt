package com.pethiio.android.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import com.pethiio.android.R
import com.pethiio.android.data.model.LookUpsResponse

class CommonMethods {


    companion object {

        @JvmStatic

        fun addRadioButton(string: String, radioGroup: RadioGroup, context: Context) {

            val radioButton = RadioButton(context)

            radioButton.text = string
            val typeface = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.resources.getFont(R.font.typo_round_regular)
            } else {
                ResourcesCompat.getFont(context, R.font.typo_round_regular)
            }


            val colorStateList = ColorStateList(
                arrayOf(
                    intArrayOf(-android.R.attr.state_checked),
                    intArrayOf(android.R.attr.state_enabled)
                ), intArrayOf(
                    ContextCompat.getColor(context, R.color.grey),  //disabled
                    ContextCompat.getColor(context, R.color.orangeButton) //enabled
                )
            )

            radioButton.buttonTintList = colorStateList

            radioButton.textSize = 14F
            radioButton.typeface = typeface
            radioButton.setTextColor(ContextCompat.getColor(context, R.color.grey))

            radioGroup.addView(radioButton)
        }

        @JvmStatic
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
        @JvmStatic
        fun getLookUpKeys(key: String, lookUpsResponse: List<LookUpsResponse>?): List<String> {

            lookUpsResponse?.forEach { it ->
                if (it.key == key) {

                    val arrayList = arrayListOf<String>()
                    it.value.forEach {
                        arrayList.add(it.key)
                    }

                    return arrayList

                }
            }
            return emptyList()
        }

        @JvmStatic
        fun getLookUpKey(key: String, value: String, lookUpsResponse: List<LookUpsResponse>?): String {

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

    fun onSNACK(view: View,snackText:String){
        //Snackbar(view)
        val snackbar = Snackbar.make(view, snackText,
            Snackbar.LENGTH_LONG)
        snackbar.setActionTextColor(Color.BLUE)
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(Color.LTGRAY)
        val textView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.BLUE)
        textView.textSize = 28f
        snackbar.show()
    }



}