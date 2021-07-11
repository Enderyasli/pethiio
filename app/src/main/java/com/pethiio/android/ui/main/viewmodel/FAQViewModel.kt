package com.pethiio.android.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pethiio.android.data.api.ResponseHandler
import com.pethiio.android.data.api.ServiceBuilder
import com.pethiio.android.data.model.AccessToken
import com.pethiio.android.data.model.login.ChangePassRequest
import com.pethiio.android.data.model.notification.NotificationListResponse
import com.pethiio.android.data.model.settings.FAQResponse
import com.pethiio.android.data.model.signup.PageData
import com.pethiio.android.utils.PreferenceHelper
import com.pethiio.android.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class FAQViewModel : ViewModel() {

    private val faqPageData = MutableLiveData<Resource<PageData>>()
    private val faqs = MutableLiveData<Resource<List<FAQResponse>>>()
    private val compositeDisposable = CompositeDisposable()
    private val responseHandler: ResponseHandler = ResponseHandler()
    private val settingsPageData = MutableLiveData<Resource<PageData>>()
    private val aboutPageData = MutableLiveData<Resource<PageData>>()
    private val changePassPageData = MutableLiveData<Resource<PageData>>()
    private val notificationSettingsPageData = MutableLiveData<Resource<PageData>>()
    private val notificationList = MutableLiveData<Resource<List<NotificationListResponse>>>()
    private val postChangePass = MutableLiveData<Resource<AccessToken>>()
    private val postNotificationChange = MutableLiveData<Resource<Response<Void>>>()


    fun fetchFAQPageData() {

        faqPageData.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getFAQPageData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        faqPageData.postValue(responseHandler.handleSuccess(loginData))
                    },
                    {
                        faqPageData.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }


    fun postChangePass(changePassRequest: ChangePassRequest) {
        postChangePass.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .postChangePass(changePassRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        PreferenceHelper.SharedPreferencesManager.getInstance().accessToken =
                            loginData.accessToken
                        PreferenceHelper.SharedPreferencesManager.getInstance().isLoggedIn =
                            true

                        postChangePass.postValue(Resource.success(loginData))
                    },
                    {
                        postChangePass.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }

    fun fetchSettingsPageData() {

        settingsPageData.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getSettingsPageData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        settingsPageData.postValue(responseHandler.handleSuccess(loginData))
                    },
                    {
                        settingsPageData.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }

    fun fetchNotificationSettingsPageData() {

        notificationSettingsPageData.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getNotificationSettingsPageData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        notificationSettingsPageData.postValue(responseHandler.handleSuccess(loginData))
                    },
                    {
                        notificationSettingsPageData.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }
    fun fetchNotificationListPageData() {

        notificationList.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getNotificationList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        notificationList.postValue(responseHandler.handleSuccess(loginData))
                    },
                    {
                        notificationList.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }

    fun fetchPostNotification(notificationListResponse: NotificationListResponse) {

        postNotificationChange.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .postNotificationChange(notificationListResponse)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        postNotificationChange.postValue(responseHandler.handleSuccess(loginData))
                    },
                    {
                        postNotificationChange.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }


    fun fetchAboutPageData() {

        aboutPageData.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getAboutPageData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        aboutPageData.postValue(responseHandler.handleSuccess(loginData))
                    },
                    {
                        aboutPageData.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }

    fun fetchChangePassPageData() {

        changePassPageData.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getChangePassPageData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        changePassPageData.postValue(responseHandler.handleSuccess(loginData))
                    },
                    {
                        changePassPageData.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }

    fun fetchFAQs() {

        faqPageData.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getFAQs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        faqs.postValue(responseHandler.handleSuccess(loginData))
                    },
                    {
                        faqs.postValue(responseHandler.handleException(it))

                    }
                )
        )
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }


    fun getFaqs(): LiveData<Resource<List<FAQResponse>>> {
        return faqs
    }

    fun getFAQPageData(): LiveData<Resource<PageData>> {
        return faqPageData
    }

    fun getChangePassPageData(): LiveData<Resource<PageData>> {
        return changePassPageData
    }

    fun getSettingsPageData(): LiveData<Resource<PageData>> {
        return settingsPageData
    }

    fun getAboutPageData(): LiveData<Resource<PageData>> {
        return aboutPageData
    }


    fun getChangePass(): LiveData<Resource<AccessToken>> {
        return postChangePass
    }

    fun getNotificationSettingsPageData(): LiveData<Resource<PageData>> {
        return notificationSettingsPageData
    }

    fun getNotificationList(): LiveData<Resource<List<NotificationListResponse>>> {
        return notificationList
    }

    fun getPostNotificationChange(): LiveData<Resource<Response<Void>>> {
        return postNotificationChange
    }

}