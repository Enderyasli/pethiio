package com.pethiio.android.ui.main.viewmodel.signup

import android.os.Handler
import androidx.annotation.Nullable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pethiio.android.data.EventBus.LikeEvent
import com.pethiio.android.data.EventBus.LoginEvent
import com.pethiio.android.data.api.ResponseHandler
import com.pethiio.android.data.api.ServiceBuilder
import com.pethiio.android.data.model.*
import com.pethiio.android.data.model.login.LoginRequest
import com.pethiio.android.data.model.petDetail.PetImageResponse
import com.pethiio.android.data.model.signup.*
import com.pethiio.android.ui.main.viewmodel.SingleLiveEvent
import com.pethiio.android.utils.PreferenceHelper
import com.pethiio.android.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import org.greenrobot.eventbus.EventBus
import retrofit2.Response


class RegisterBaseViewModel : ViewModel() {

    private val loginPageData = MutableLiveData<Resource<PageData>>()
    val register = MutableLiveData<Resource<PageData>>()
    var postRegister = SingleLiveEvent<Resource<RegisterResponse>>()
    val postLogin = SingleLiveEvent<Resource<LoginResponse>>()
    val postRegisterInfo = MutableLiveData<Resource<AccessToken>>()
    val postRegisterAvatar = MutableLiveData<Resource<AccessToken>>()
    val postPetPhoto = SingleLiveEvent<Resource<AccessToken>>()
    val postPetAdd = SingleLiveEvent<Resource<PetAddResponse>>()
    val postPetEdit = SingleLiveEvent<Resource<Response<Void>>>()
    val petList = MutableLiveData<Resource<List<PetListResponse>>>()
    val petListPageData = MutableLiveData<Resource<List<PethiioResponse>>>()

    private val fields = MutableLiveData<List<PethiioResponse>>()
    private val registerFields = MutableLiveData<List<PethiioResponse>>()
    private val registerLookUps = MutableLiveData<List<LookUpsResponse>>()
    private val registerDetailFields = MutableLiveData<List<PethiioResponse>>()
    private val registerDetailLookUps = MutableLiveData<List<LookUpsResponse>>()
    private val addAnimalImageFields = MutableLiveData<List<PethiioResponse>>()
    private val addAnimalResponse = SingleLiveEvent<Resource<PageData>>()
    private val petAddDetail = MutableLiveData<AnimalDetailResponse>()
    private val petDetail = SingleLiveEvent<Resource<PetAdd>>()
    private val petAddPhotos = MutableLiveData<Resource<List<PetImageResponse>>>()


    private val compositeDisposable = CompositeDisposable()
    private var accessToken: String = ""
    private val responseHandler: ResponseHandler = ResponseHandler()


    //region Login

    fun fetchLogin() {
        loginPageData.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getLoginPageData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ loginData ->
                    loginPageData.postValue(Resource.success(loginData))
                    fields.postValue(loginData.fields)
                }, {
                    loginPageData.postValue(Resource.error(it.message, null))
                })
        )
    }

    fun postLogin(loginRequest: LoginRequest) {
        postLogin.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .postLogin(loginRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        if (loginData.emailVerified && loginData.registrationCompleted) {
                            PreferenceHelper.SharedPreferencesManager.getInstance().accessToken =
                                loginData.accessToken
                            PreferenceHelper.SharedPreferencesManager.getInstance().isLoggedIn =
                                true



                        }
                        postLogin.postValue(Resource.success(loginData))
                    },
                    {
                        postLogin.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }

    //endregion

    //region Register

    fun fetchRegister() {
        register.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService().getRegisterPageData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ registerData ->
                    register.postValue(Resource.success(registerData))
                    registerFields.postValue(registerData.fields)
                    registerLookUps.postValue(registerData.lookups)
                }, {
                    register.postValue(responseHandler.handleException(it))
                })
        )
    }

    fun fetchRegisterDetail() {
        register.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService().getRegisterDetailPageData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ registerData ->
                    register.postValue(Resource.success(registerData))
                    registerDetailFields.postValue(registerData.fields)
                    registerDetailLookUps.postValue(registerData.lookups)
                }, {
                    register.postValue(responseHandler.handleException(it))
                })
        )
    }

    fun postRegister(register: Register) {
        postRegister.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService().postRegister(register)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { registerData ->
                        postRegister.postValue(Resource.success(registerData))
                        accessToken = registerData.accessToken
                        PreferenceHelper.SharedPreferencesManager.getInstance().accessToken =
                            accessToken

                    },
                    {
                        postRegister.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }

    fun postRegisterInfo(registerInfo: RegisterInfo) {
        postRegisterInfo.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .postRegisterInfo(registerInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { registerData ->
                        PreferenceHelper.SharedPreferencesManager.getInstance().accessToken =
                            registerData.accessToken

                        postRegisterInfo.postValue(Resource.success(registerData))
                    },
                    {
                        postRegisterInfo.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }

    fun postRegisterAvatar(multipart: MultipartBody.Part) {
        postRegisterAvatar.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .postRegisterAvatar(multipart)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { registerData ->
                        PreferenceHelper.SharedPreferencesManager.getInstance().accessToken =
                            registerData.accessToken
                        PreferenceHelper.SharedPreferencesManager.getInstance().isLoggedIn =
                            true
                        postRegisterAvatar.postValue(Resource.success(registerData))
                    },
                    {
                        postRegisterAvatar.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }

    //endregion

    //region Pet

    fun fetchAnimalAddPhotoPageData() {
        register.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService().getPetAddPhotoPageData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ registerData ->
                    register.postValue(Resource.success(registerData))
                    addAnimalImageFields.postValue(registerData.fields)
                }, {
                    register.postValue(Resource.error(it.message, null))
                })
        )
    }

    fun fetchPetAddPageData() {
        addAnimalResponse.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService().getPetInfoPageData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ registerData ->
                    addAnimalResponse.postValue(Resource.success(registerData))
                }, {
                    addAnimalResponse.postValue(responseHandler.handleException(it))
                })
        )
    }

    fun fetchPetPhotos(petId: Int) {
        petAddPhotos.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService().getPetPhotos(petId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ registerData ->
                    petAddPhotos.postValue(responseHandler.handleSuccess(registerData))
                }, {
                    petAddPhotos.postValue(responseHandler.handleException(it))
                })
        )
    }

    fun fetchPetListPageData() {
        petListPageData.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService().getPetListInfoPageData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ registerData ->
                    petListPageData.postValue(Resource.success(registerData.fields))
                }, {
                    petListPageData.postValue(responseHandler.handleException(it))
                })
        )
    }

    fun fetchAnimalDetail(animalId: String) {
        register.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService().getAnimalDetail(animalId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fun(registerData: AnimalDetailResponse) {
                    petAddDetail.postValue(registerData)
                }, {
                    register.postValue(responseHandler.handleException(it))
                })
        )
    }

    fun postPetPhoto(@Nullable multipart: List<MultipartBody.Part?>) {
        postPetPhoto.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .postPetPhoto(
                    PreferenceHelper.SharedPreferencesManager.getInstance().petId,
                    multipart
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        if (it.code().toString().startsWith("2"))
                            postPetPhoto.postValue(Resource.success(null))
                        else
                            postPetPhoto.postValue(Resource.error(it.message(), null))


                    },
                    {
                        postPetPhoto.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }

    fun postPetAdd(petAdd: PetAdd) {
        postPetAdd.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService().postPetAdd(petAdd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { registerData ->
                        PreferenceHelper.SharedPreferencesManager.getInstance().petId =
                            registerData.id
                        postPetAdd.postValue(Resource.success(registerData))
                    },
                    {
                        postPetAdd.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }

    fun postPetEdit(petEdit: PetEdit) {
        postPetEdit.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService().postPetEdit(petEdit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { registerData ->

                        postPetEdit.postValue(Resource.success(registerData))
                    },
                    {
                        postPetEdit.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }

    fun getUserPetDetail(animalId: String) {
        petDetail.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService().getPetEditDetail(animalId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { registerData ->
                        petDetail.postValue(Resource.success(registerData))
                    },
                    {
                        petDetail.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }

    fun fetchPetList() {
        petList.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getPetList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { registerData ->
                        petList.postValue(Resource.success(registerData))
                    },
                    {
                        petList.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }


    //endregion

    //region LiveData getter

    fun getLoginPageData(): LiveData<Resource<PageData>> {
        return loginPageData
    }

    fun getPostLogin(): LiveData<Resource<LoginResponse>> {
        return postLogin
    }

    fun getFields(): LiveData<List<PethiioResponse>> {
        return fields
    }

    fun getRegisterFields(): LiveData<List<PethiioResponse>> {
        return registerFields
    }

    fun getRegisterLookUps(): LiveData<List<LookUpsResponse>> {
        return registerLookUps
    }

    fun getRegisterDetailFields(): LiveData<List<PethiioResponse>> {
        return registerDetailFields
    }

    fun getRegisterDetailLookUps(): LiveData<List<LookUpsResponse>> {
        return registerDetailLookUps
    }

    fun getAddImageFields(): LiveData<List<PethiioResponse>> {
        return addAnimalImageFields
    }

    fun getPetAddPageData(): LiveData<Resource<PageData>> {
        return addAnimalResponse
    }

    fun getUserPetDetail(): LiveData<Resource<PetAdd>> {
        return petDetail
    }

    fun getAddAnimalDetails(): LiveData<AnimalDetailResponse> {
        return petAddDetail
    }


    fun getPetList(): LiveData<Resource<List<PetListResponse>>> {
        return petList
    }

    fun getPetPhotos(): LiveData<Resource<List<PetImageResponse>>> {
        return petAddPhotos
    }

    fun getPostPetEdit(): LiveData<Resource<Response<Void>>> {
        return postPetEdit
    }

    //endregion

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }


}