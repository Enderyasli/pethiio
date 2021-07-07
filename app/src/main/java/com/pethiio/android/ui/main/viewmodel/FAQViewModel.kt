package com.pethiio.android.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pethiio.android.data.api.ResponseHandler
import com.pethiio.android.data.api.ServiceBuilder
import com.pethiio.android.data.model.petDetail.PetSearchDetailResponse
import com.pethiio.android.data.model.petDetail.PetSearchOwnerDetailResponse
import com.pethiio.android.data.model.settings.FAQResponse
import com.pethiio.android.data.model.signup.PageData
import com.pethiio.android.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class FAQViewModel : ViewModel() {

    private val faqPageData = MutableLiveData<Resource<PageData>>()
    private val faqs = MutableLiveData<Resource<List<FAQResponse>>>()
    private val compositeDisposable = CompositeDisposable()
    private val responseHandler: ResponseHandler = ResponseHandler()
    private val settingsPageData = MutableLiveData<Resource<PageData>>()
    private val aboutPageData = MutableLiveData<Resource<PageData>>()


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

    fun getSettingsPageData(): LiveData<Resource<PageData>> {
        return settingsPageData
    }
    fun getAboutPageData(): LiveData<Resource<PageData>> {
        return aboutPageData
    }

}