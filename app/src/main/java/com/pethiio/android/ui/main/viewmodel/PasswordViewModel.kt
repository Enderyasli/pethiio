package com.pethiio.android.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pethiio.android.data.api.ResponseHandler
import com.pethiio.android.data.api.ServiceBuilder
import com.pethiio.android.data.model.resetPass.PinVerificationRequest
import com.pethiio.android.data.model.resetPass.ResetPassDemand
import com.pethiio.android.data.model.resetPass.ResetPassDemandResponse
import com.pethiio.android.data.model.resetPass.ResetPasswordRequest
import com.pethiio.android.data.model.signup.PageData
import com.pethiio.android.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class PasswordViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val responseHandler: ResponseHandler = ResponseHandler()
    private val resetPassDemandPageData = MutableLiveData<Resource<PageData>>()
    private val pinVerifyPageData = MutableLiveData<Resource<PageData>>()
    private val pinResetPasswordPageData = MutableLiveData<Resource<PageData>>()
    private val resetPasswordDonePageData = MutableLiveData<Resource<PageData>>()
    private val postResetDemand = MutableLiveData<Resource<ResetPassDemandResponse>>()
    private val postResetPassword = MutableLiveData<Resource<Response<Void>>>()
    private val pinVerificationResponse = MutableLiveData<Resource<Response<Void>>>()


    fun postResetDemandPass(resetPassDemand: ResetPassDemand) {
        postResetDemand.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .postResetDemand(resetPassDemand)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        postResetDemand.postValue(Resource.success(loginData))
                    },
                    {
                        postResetDemand.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }


    fun postPostPinVerification(pinVerificationRequest: PinVerificationRequest) {
        pinVerificationResponse.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .postPinVerification(pinVerificationRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        if (loginData.code().toString().startsWith("2"))
                            pinVerificationResponse.postValue(Resource.success(loginData))
                    },
                    {
                        pinVerificationResponse.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }

    fun postResetPassword(resetPasswordRequest: ResetPasswordRequest) {
        postResetPassword.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .postResetPassword(resetPasswordRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        postResetPassword.postValue(Resource.success(loginData))
                    },
                    {
                        postResetPassword.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }


    fun fetchResetPassDemandPageData() {

        resetPassDemandPageData.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getResetPassDemandPageData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        resetPassDemandPageData.postValue(responseHandler.handleSuccess(loginData))
                    },
                    {
                        resetPassDemandPageData.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }

    fun fetchPinVerifiedPageData() {

        pinVerifyPageData.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getPinVerifyPageData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        pinVerifyPageData.postValue(responseHandler.handleSuccess(loginData))
                    },
                    {
                        pinVerifyPageData.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }

    fun fetchResetPasswordLastPageData() {

        pinResetPasswordPageData.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getResetPassPageData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        pinResetPasswordPageData.postValue(responseHandler.handleSuccess(loginData))
                    },
                    {
                        pinResetPasswordPageData.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }

    fun fetchResetPasswordDonePageData() {

        resetPasswordDonePageData.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getResetPasswordDonePageData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        resetPasswordDonePageData.postValue(responseHandler.handleSuccess(loginData))
                    },
                    {
                        resetPasswordDonePageData.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }


    fun getResetPassDemandPageData(): LiveData<Resource<PageData>> {
        return resetPassDemandPageData
    }

    fun getPinVerifiedPageData(): LiveData<Resource<PageData>> {
        return pinVerifyPageData
    }

    fun getPostResetDemand(): LiveData<Resource<ResetPassDemandResponse>> {
        return postResetDemand
    }

    fun getPinVerificationResponse(): LiveData<Resource<Response<Void>>> {
        return pinVerificationResponse
    }

    fun getResetPasswordResponse(): LiveData<Resource<Response<Void>>> {
        return postResetPassword
    }

    fun getPinResetPasswordPageData(): LiveData<Resource<PageData>> {
        return pinResetPasswordPageData
    }
    fun getResetPasswordDonePageData(): LiveData<Resource<PageData>> {
        return resetPasswordDonePageData
    }

}