package com.pethiio.android.ui.main.viewmodel.signup

import androidx.annotation.Nullable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pethiio.android.data.api.ServiceBuilder
import com.pethiio.android.data.model.*
import com.pethiio.android.data.model.login.LoginRequest
import com.pethiio.android.data.model.signup.Login
import com.pethiio.android.data.model.signup.Register
import com.pethiio.android.data.model.signup.RegisterInfo
import com.pethiio.android.data.repository.MainRepository
import com.pethiio.android.utils.PreferenceHelper
import com.pethiio.android.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody

class RegisterBaseViewModel : ViewModel() {

    private val loginPageData = MutableLiveData<Resource<Login>>()
    val register = MutableLiveData<Resource<Login>>()
    val postRegister = MutableLiveData<Resource<AccessToken>>()
    val postLogin = MutableLiveData<Resource<AccessToken>>()
    val postRegisterInfo = MutableLiveData<Resource<AccessToken>>()
    val postRegisterAvatar = MutableLiveData<Resource<AccessToken>>()
    val postPetPhoto = MutableLiveData<Resource<AccessToken>>()
    val postPetAdd = MutableLiveData<Resource<PetAddResponse>>()
    val petList = MutableLiveData<Resource<List<PetListResponse>>>()
    val petListPageData = MutableLiveData<Resource<List<PethiioResponse>>>()

    private val fields = MutableLiveData<List<PethiioResponse>>()
    private val registerFields = MutableLiveData<List<PethiioResponse>>()
    private val registerLookUps = MutableLiveData<List<LookUpsResponse>>()
    private val registerDetailFields = MutableLiveData<List<PethiioResponse>>()
    private val registerDetailLookUps = MutableLiveData<List<LookUpsResponse>>()
    private val addAnimalLookUps = MutableLiveData<List<LookUpsResponse>>()
    private val addAnimalImageFields = MutableLiveData<List<PethiioResponse>>()
    private val addAnimalFields = MutableLiveData<List<PethiioResponse>>()
    private val petAddDetail = MutableLiveData<AnimalDetailResponse>()

    private val compositeDisposable = CompositeDisposable()
    private var accessToken: String = ""

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
                        PreferenceHelper.SharedPreferencesManager.getInstance().accessToken =
                            loginData.accessToken
                        PreferenceHelper.SharedPreferencesManager.getInstance().isLoggedIn =
                            true

                        postLogin.postValue(Resource.success(loginData))
                    },
                    {
                        postLogin.postValue(Resource.error(it.message, null))
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
                    register.postValue(Resource.error(it.message, null))
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
                    register.postValue(Resource.error(it.message, null))
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
                        postRegister.postValue(Resource.error(it.message, null))
                    }
                )
        )
    }

    fun postRegisterInfo(registerInfo: RegisterInfo) {
        postRegister.postValue(Resource.loading(null))
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
                        postRegisterInfo.postValue(Resource.error(it.message, null))
                    }
                )
        )
    }

    fun postRegisterAvatar(multipart: MultipartBody.Part) {
        postRegister.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .postRegisterAvatar(multipart)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { registerData ->
                        PreferenceHelper.SharedPreferencesManager.getInstance().accessToken =
                            registerData.accessToken
                        postRegisterAvatar.postValue(Resource.success(registerData))
                    },
                    {
                        postRegisterAvatar.postValue(Resource.error(it.message, null))
                    }
                )
        )
    }

    //endregion

    //region Pet
    fun fetchAnimalAddPhoto() {
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
        register.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService().getPetInfoPageData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ registerData ->
                    register.postValue(Resource.success(registerData))
                    addAnimalFields.postValue(registerData.fields)
                    addAnimalLookUps.postValue(registerData.lookups)
                }, {
                    register.postValue(Resource.error(it.message, null))
                })
        )
    }

    fun fetchPetListPageData() {
        register.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService().getPetListInfoPageData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ registerData ->
                    petListPageData.postValue(Resource.success(registerData.fields))
                }, {
                    petListPageData.postValue(Resource.error(it.message, null))
                })
        )
    }

    fun fetchAnimalDetail(animalId: String) {
        register.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService().getPetDetail(animalId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fun(registerData: AnimalDetailResponse) {
                    petAddDetail.postValue(registerData)
                }, {
                    register.postValue(Resource.error(it.message, null))
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
                        postPetPhoto.postValue(Resource.error(it.message, null))
                    }
                )
        )
    }

    fun postPetAdd(petAdd: PetAdd) {
        postRegister.postValue(Resource.loading(null))
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
                        postPetAdd.postValue(Resource.error(it.message, null))
                    }
                )
        )
    }

    fun fetchPetList() {
        postRegister.postValue(Resource.loading(null))
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
                        petList.postValue(Resource.error(it.message, null))
                    }
                )
        )
    }


    //endregion

    //region LiveData getter

    fun getLoginPageData(): LiveData<Resource<Login>> {
        return loginPageData
    }

    fun getPostLogin(): LiveData<Resource<AccessToken>> {
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

    fun getAddAnimalFields(): LiveData<List<PethiioResponse>> {
        return addAnimalFields
    }

    fun getAddAnimalDetails(): LiveData<AnimalDetailResponse> {
        return petAddDetail
    }

    fun getAddAnimalLookUps(): LiveData<List<LookUpsResponse>> {
        return addAnimalLookUps
    }

    fun getPetList(): LiveData<Resource<List<PetListResponse>>> {
        return petList
    }

    //endregion

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }


}