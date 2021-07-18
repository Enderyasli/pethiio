package com.pethiio.android.data.api

import com.pethiio.android.data.model.*
import com.pethiio.android.data.model.chat.ChatListResponse
import com.pethiio.android.data.model.chat.ChatRoomResponse
import com.pethiio.android.data.model.filter.PetSearchFilterResponse
import com.pethiio.android.data.model.filter.SearchFilterRequest
import com.pethiio.android.data.model.login.ChangePassRequest
import com.pethiio.android.data.model.login.LoginRequest
import com.pethiio.android.data.model.member.LocationsRequest
import com.pethiio.android.data.model.member.MemberListResponse
import com.pethiio.android.data.model.member.PetSearchRequest
import com.pethiio.android.data.model.notification.NotificationListResponse
import com.pethiio.android.data.model.petDetail.PetImageResponse
import com.pethiio.android.data.model.petDetail.PetSearchDetailResponse
import com.pethiio.android.data.model.petDetail.PetSearchOwnerDetailResponse
import com.pethiio.android.data.model.report.ReportRequest
import com.pethiio.android.data.model.report.SupportRequest
import com.pethiio.android.data.model.resetPass.PinVerificationRequest
import com.pethiio.android.data.model.resetPass.ResetPassDemand
import com.pethiio.android.data.model.resetPass.ResetPassDemandResponse
import com.pethiio.android.data.model.resetPass.ResetPasswordRequest
import com.pethiio.android.data.model.settings.FAQResponse
import com.pethiio.android.data.model.signup.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface PethiioServices {

    //region login
    @GET("page/login/info")
    fun getLoginPageData(): Observable<PageData>

    @POST("page/login")
    fun postLogin(@Body loginRequest: LoginRequest): Observable<LoginResponse>

    //endregion

    //region Register

    @POST("page/register")
    fun postRegister(@Body register: Register): Observable<RegisterResponse>

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


    @GET("page/pet-add-photo/{petId}")
    fun getPetPhotos(
        @Path("petId") petId: Int
    ): Observable<List<PetImageResponse>>


    @Multipart
    @POST("page/pet-add-photo/{animalId}")
    fun postPetPhotoEdit(
        @Path("animalId") animalId: Int,
        @Part files: List<MultipartBody.Part?>
    ): Observable<Response<Void>>

    @GET("page/pet-list")
    fun getPetList(): Observable<List<PetListResponse>>

    //endregion

    //region pet Match

    //endregion

    @GET("page/pet-match/info")
    fun getPetMatchPageData(): Observable<PageData>

    //region Pet Detail

    @GET("page/pet-search-detail/info")
    fun getPetSearchDetailPageData(): Observable<PageData>

    @GET("page/pet-detail/info")
    fun getPetDetailPageData(): Observable<PageData>


    @GET("page/pet-search-detail/{animalId}")
    fun getPetSearchDetail(
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

    @POST("page/pet-search-filter")
    fun postSearchFilter(@Body filterRequest: SearchFilterRequest): Observable<Response<Void>>


    //endregion

    //region Member

    @POST("/api/page/member-location")
    fun postLocations(@Body locationsRequest: LocationsRequest): Observable<Response<Void>>

    @GET("page/pet-search/members")
    fun getMemberList(): Observable<List<MemberListResponse>>

    @POST("page/pet-search")
    fun postPetSearch(@Body petSearchRequest: PetSearchRequest): Observable<Response<Void>>

    //endregion

    //region Pet Search

    @GET("page/pet-search/{animalId}")
    fun getPetSearch(@Path("animalId") animalId: Int): Observable<Content>

    @GET("page/pet-detail/{animalId}")
    fun getPetDetail(
        @Path("animalId") animalId: String
    ): Observable<PetAdd>


    @GET("page/pet-search/info")
    fun getPetSearchPageData(): Observable<PageData>

    //endregion

    //region pet edit

    @GET("page/pet-edit/{petId}")
    fun getPetEditDetail(@Path("petId") petId: String): Observable<PetAdd>

    @POST("page/pet-edit")
    fun postPetEdit(@Body petAdd: PetEdit): Observable<Response<Void>>

    @DELETE("page/pet-detail/{petId}")
    fun postPetDelete(@Path("petId") petId: Int): Observable<Response<Void>>


    //endregion

    //region Report

    @GET("page/report/info")
    fun getReportPageData(): Observable<PageData>

    @POST("page/report")
    fun postReport(@Body reportRequest: ReportRequest): Observable<Response<Void>>

    //endregion

    //region FAQ

    @GET("page/faq/getAll")
    fun getFAQs(): Observable<List<FAQResponse>>

    @GET("page/faq/info")
    fun getFAQPageData(): Observable<PageData>

    @GET("page/support/info")
    fun getSupportPageData(): Observable<PageData>

    @GET("page/support-success/info")
    fun getSupportSuccessPageData(): Observable<PageData>


    @POST("page/support")
    fun postSupport(@Body supportRequest: SupportRequest): Observable<Response<Void>>

    //endregion


    //region Settings

    @GET("page/settings/info")
    fun getSettingsPageData(): Observable<PageData>

    @GET("page/about/info")
    fun getAboutPageData(): Observable<PageData>

    @GET("/api/page/change-password/info")
    fun getChangePassPageData(): Observable<PageData>

    @POST("page/change-password")
    fun postChangePass(@Body changePassRequest: ChangePassRequest): Observable<AccessToken>

    @GET("page/notification-settings/info")
    fun getNotificationSettingsPageData(): Observable<PageData>

    @GET("/api/page/notification-settings")
    fun getNotificationList(): Observable<List<NotificationListResponse>>

    @POST("/api/page/notification-settings")
    fun postNotificationChange(@Body notificationListResponse: NotificationListResponse): Observable<Response<Void>>


    //endregion

    //region Reset Password

    @GET("page/reset-password-demand/info")
    fun getResetPassDemandPageData(): Observable<PageData>

    @POST("page/reset-password-demand")
    fun postResetDemand(@Body resetPassDemand: ResetPassDemand): Observable<ResetPassDemandResponse>


    @GET("page/reset-password/info")
    fun getResetPassPageData(): Observable<PageData>

    @GET("page/reset-password-done/info")
    fun getResetPasswordDonePageData(): Observable<PageData>


    @GET("page/pin-verification/info")
    fun getPinVerifyPageData(): Observable<PageData>

    @POST("page/pin-verification/reset-password")
    fun postPinVerification(@Body pinVerificationRequest: PinVerificationRequest): Observable<Response<Void>>

    @POST("page/pin-verification/email")
    fun postEmailVerification(@Body pinVerificationRequest: PinVerificationRequest): Observable<Response<Void>>


    @POST("page/reset-password")
    fun postResetPassword(@Body resetPasswordRequest: ResetPasswordRequest): Observable<Response<Void>>

    //endregion

    //region Location

    @GET("page/member-location/info")
    fun getLocationAccessPageData(): Observable<PageData>

    //endregion

    //region Chat

    @GET("page/chat-list/info")
    fun getChatListPageData(): Observable<PageData>

    @GET("page/chat/info")
    fun getChatPageData(): Observable<PageData>


    @GET("page/chat-list/{memberId}")
    fun getChatList(
        @Path("memberId") memberId: Int,
    ): Observable<List<ChatListResponse>>

    @GET("page/chat/{roomId}")
    fun getChatRoom(
        @Path("roomId") roomId: Int,
    ): Observable<List<ChatRoomResponse>>

    //endregion

    //region Home

    @GET("page/home/info")
    fun getHomePageData(): Observable<PageData>

    //endregion


    //region ChatList SocketIO

    @GET("page/chat-list")
    fun getAllChatList(): Observable<List<ChatListResponse>>

    //endregion

}
