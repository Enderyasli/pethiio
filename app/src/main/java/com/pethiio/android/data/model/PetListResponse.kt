package com.pethiio.android.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class PetListResponse() : Parcelable {
    @SerializedName("id")
    val id: Int = 0
    @SerializedName("name")
    val name: String = ""
    @SerializedName("photo")
    val photo: String = ""

    constructor(parcel: Parcel) : this() {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PetListResponse> {
        override fun createFromParcel(parcel: Parcel): PetListResponse {
            return PetListResponse(parcel)
        }

        override fun newArray(size: Int): Array<PetListResponse?> {
            return arrayOfNulls(size)
        }
    }
}