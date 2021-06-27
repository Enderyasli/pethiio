package com.pethiio.android.data.api

import com.pethiio.android.data.model.*
import com.pethiio.android.data.model.signup.Login
import com.pethiio.android.data.model.signup.RegisterInfo
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface RetrofitServices {

    //region RegisterInfo
    @GET("page/register-detail/info")
    fun getRegisterInfoPageData(): Observable<Login>

    @POST("page/register-detail")
    fun postRegisterInfo(@Body registerInfo: RegisterInfo): Observable<AccessToken>

    @Multipart
    @POST("page/register-detail/avatar")
    fun postRegisterAvatar(
        @Part file: MultipartBody.Part
    ): Observable<AccessToken>
    //endregion

    //region Pet
    @Multipart
    @POST("page/pet-add-photo/{animalId}")
    fun postPetPhoto(
        @Path("animalId") animalId: Int,
        @Part files: List<MultipartBody.Part?>
    ): Observable<Response<Void>>

    @POST("page/pet-add")
    fun postPetAdd(@Body petAdd: PetAdd): Observable<PetAddResponse>

    @GET("page/pet-add/info")
    fun getPetInfoPageData(): Observable<Login>

    @GET("page/pet-list/info")
    fun getPetListInfoPageData(): Observable<Login>

    @GET("page/pet-add/animal-detail/{animalId}")
    fun getPetDetail(@Path("animalId") animalId: String): Observable<AnimalDetailResponse>


    @GET("page/pet-list")
    fun getPetList(): Observable<List<PetListResponse>>

    //endregion


}
