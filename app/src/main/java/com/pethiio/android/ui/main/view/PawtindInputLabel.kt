package com.pethiio.android.ui.main.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.pethiio.android.R


class PawtindInputLabel @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var titleTv: TextView
    private var inputTv: TextView

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.common_rounded_input_tv, this, true)

        titleTv = findViewById(R.id.titleTv)
        inputTv = findViewById(R.id.placeholder_tv)

        val semiBold = ResourcesCompat.getFont(context, R.font.poppins_semibold)
        val medium = ResourcesCompat.getFont(context, R.font.poppins_medium)
        titleTv.typeface = semiBold
        inputTv.typeface = medium
    }


}