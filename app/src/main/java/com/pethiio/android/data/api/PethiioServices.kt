package com.pethiio.android.data.api

import com.pethiio.android.data.model.*
import com.pethiio.android.data.model.filter.PetSearchFilterResponse
import com.pethiio.android.data.model.login.LoginRequest
import com.pethiio.android.data.model.member.LocationsRequest
import com.pethiio.android.data.model.member.MemberListResponse
import com.pethiio.android.data.model.member.PetSearchRequest
import com.pethiio.android.data.model.member.PetSearchResponse
import com.pethiio.android.data.model.petDetail.PetSearchDetailResponse
import com.pethiio.android.data.model.petDetail.PetSearchOwnerDetailResponse
import com.pethiio.android.data.model.signup.PageData
import com.pethiio.android.data.model.signup.Register
import com.pethiio.android.data.model.signup.RegisterInfo
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface PethiioServices {

    //region login
    @GET("page/login/info")
    fun getLoginPageData(): Observable<PageData>

    @POST("page/login")
    fun postLogin(@Body loginRequest: LoginRequest): Observable<AccessToken>

    //endregion

    //region Register

    @POST("page/register")
    fun postRegister(@Body register: Register): Observable<AccessToken>

    @GET("page/register/info")
    fun getRegisterPageData(): Observable<PageData>

    @GET("page/register-detail/info")
    fun getRegisterDetailPageData(): Observable<PageData>

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
    fun getPetInfoPageData(): Observable<PageData>

    @GET("page/pet-add-photo/info")
    fun getPetAddPhotoPageData(): Observable<PageData>

    @GET("page/pet-list/info")
    fun getPetListInfoPageData(): Observable<PageData>

    @GET("page/pet-add/animal-detail/{animalId}")
    fun getAnimalDetail(@Path("animalId") animalId: String): Observable<AnimalDetailResponse>


    @GET("page/pet-list")
    fun getPetList(): Observable<List<PetListResponse>>

    //endregion

    //region Pet Detail

    @GET("page/pet-search-detail/info")
    fun getPetSearchDetailPageData(): Observable<PageData>


    @GET("page/pet-search-detail/{animalId}")
    fun getPetDetail(
        @Path("animalId") animalId: String,
        @Query("sourceMemberId") sourceMemberId: Int
    ): Observable<PetSearchDetailResponse>

    @GET("page/pet-search-detail/owner/{userId}")
    fun getPetOwnerDetail(
        @Path("userId") userId: String
    ): Observable<PetSearchOwnerDetailResponse>

    //endregion

    //region Filter

    @GET("page/pet-search-filter")
    fun getPetSearchList(): Observable<PetSearchFilterResponse>

    @GET("page/pet-search-filter/info")
    fun getPetSearchListPageData(): Observable<PageData>


    //endregion

    //region Member

    @POST("locations")
    fun postLocations(@Body locationsRequest: LocationsRequest): Observable<Response<Void>>

    @GET("page/pet-search/members")
    fun getMemberList(): Observable<List<MemberListResponse>>

    @POST("page/pet-search")
    fun postPetSearch(@Body petSearchRequest: PetSearchRequest): Observable<Response<Void>>

    //endregion

    //region Pet Search

    @GET("page/pet-search/{animalId}")
    fun getPetSearch(@Path("animalId") animalId: Int): Observable<List<PetSearchResponse>>

    //endregion

    //region Report

    @GET("page/report/info")
    fun getReportPageData(): Observable<PageData>

    //endregion

}
