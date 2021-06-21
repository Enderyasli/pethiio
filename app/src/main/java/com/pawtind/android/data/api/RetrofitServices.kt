package com.pawtind.android.data.api

import com.pawtind.android.data.model.AccessToken
import com.pawtind.android.data.model.PetAdd
import com.pawtind.android.data.model.PetAddResponse
import com.pawtind.android.data.model.signup.Login
import com.pawtind.android.data.model.signup.RegisterInfo
import io.reactivex.Observable
import retrofit2.http.*

interface RetrofitServices {

    @GET("page/register/info")
    fun getRegisterInfoPageData(): Observable<Login>

    @POST("page/register-detail")
    fun postRegisterInfo(@Body registerInfo: RegisterInfo): Observable<AccessToken>

    @POST("page/pet-add")
    fun postPetAdd(@Body petAdd: PetAdd): Observable<PetAddResponse>


}
