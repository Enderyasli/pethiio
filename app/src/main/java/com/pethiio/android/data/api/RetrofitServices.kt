package com.pethiio.android.data.api

import com.pethiio.android.data.model.AccessToken
import com.pethiio.android.data.model.AnimalDetailResponse
import com.pethiio.android.data.model.PetAdd
import com.pethiio.android.data.model.PetAddResponse
import com.pethiio.android.data.model.signup.Login
import com.pethiio.android.data.model.signup.RegisterInfo
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

interface RetrofitServices {

    @GET("page/register/info")
    fun getRegisterInfoPageData(): Observable<Login>

    @POST("page/register-detail")
    fun postRegisterInfo(@Body registerInfo: RegisterInfo): Observable<AccessToken>

    @Multipart
    @POST("page/register-detail/avatar")
    fun postRegisterAvatar(
        @Part file: MultipartBody.Part
    ): Observable<AccessToken>

    @Multipart
    @POST("page/pet-add-photo/{animalId}")
    fun postPetPhoto(
        @Path("animalId") animalId: Int,
        @Part files: List<MultipartBody.Part>
    ): Observable<AccessToken>

    @POST("page/pet-add")
    fun postPetAdd(@Body petAdd: PetAdd): Observable<PetAddResponse>

    @GET("page/pet-add/animal-detail/{animalId}")
    fun getPetDetail(@Path("animalId") animalId: String): Observable<AnimalDetailResponse>


}
