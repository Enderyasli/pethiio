package com.pethiio.android.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pethiio.android.data.api.ResponseHandler
import com.pethiio.android.data.api.ServiceBuilder
import com.pethiio.android.data.model.signup.PageData
import com.pethiio.android.data.model.user.UserDetailResponse
import com.pethiio.android.data.model.user.UserEditRequest
import com.pethiio.android.utils.PreferenceHelper
import com.pethiio.android.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import retrofit2.Response

class UserDetailViewModel : ViewModel() {

    private val userDetailResponse = MutableLiveData<Resource<UserDetailResponse>>()
    private val postUserEditResponse = MutableLiveData<Resource<Response<Void>>>()
    private val postUserAvatarResponse = MutableLiveData<Resource<Response<Void>>>()
    private val userEditPageData = MutableLiveData<Resource<PageData>>()
    private val compositeDisposable = CompositeDisposable()
    private val responseHandler: ResponseHandler = ResponseHandler()


    fun fetchUserDetail() {
        userDetailResponse.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getUserDetail()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        userDetailResponse.postValue(Resource.success(loginData))
                    },
                    {
                        userDetailResponse.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }

    fun postUserEdit(userEditRequest: UserEditRequest) {
        userDetailResponse.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .postUserEdit(userEditRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        postUserEditResponse.postValue(Resource.success(loginData))
                    },
                    {
                        postUserEditResponse.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }

    fun postUserAvatar(multipart: MultipartBody.Part) {
        postUserAvatarResponse.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .postUserAvatar(multipart)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { registerData ->
                        postUserAvatarResponse.postValue(Resource.success(registerData))
                    },
                    {
                        postUserAvatarResponse.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }

    fun fetchUserEditPageData() {

        userEditPageData.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getUserEditPageData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        userEditPageData.postValue(Resource.success(loginData))
                    },
                    {
                        userEditPageData.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getUserDetail(): LiveData<Resource<UserDetailResponse>> {
        return userDetailResponse
    }

    fun getUserEditResponse(): LiveData<Resource<Response<Void>>> {
        return postUserEditResponse
    }

    fun getUserAvatarResponse(): LiveData<Resource<Response<Void>>> {
        return postUserAvatarResponse
    }


    fun getUserEditPageData(): LiveData<Resource<PageData>> {
        return userEditPageData
    }

}