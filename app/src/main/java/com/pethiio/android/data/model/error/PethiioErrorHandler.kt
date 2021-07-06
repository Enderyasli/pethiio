package com.pethiio.android.data.model.error

import com.google.gson.annotations.SerializedName

data class PethiioErrorHandler(

    @SerializedName("apierror")
    val apierror: PethiioError


)