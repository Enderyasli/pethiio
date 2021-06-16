package com.pawtind.android.data.model.signup

import com.google.gson.annotations.SerializedName
import com.pawtind.android.data.model.PawtindResponse

data class Register(

    @SerializedName("email")
    val email: String = "",
    @SerializedName("firstName")
    val firstName: String = "",
    @SerializedName("lastName")
    val lastName: String = "",
    @SerializedName("password")
    val password: String = ""
)