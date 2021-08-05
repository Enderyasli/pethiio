package com.pethiio.android.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pethiio.android.data.api.ResponseHandler
import com.pethiio.android.data.api.ServiceBuilder
import com.pethiio.android.data.model.signup.PageData
import com.pethiio.android.data.model.vet.VetResponse
import com.pethiio.android.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class VetViewModel : ViewModel() {

    private val vetPageData = MutableLiveData<Resource<PageData>>()
    private val vetInfos = MutableLiveData<Resource<List<VetResponse>>>()
    private val compositeDisposable = CompositeDisposable()
    private val responseHandler: ResponseHandler = ResponseHandler()


    init {
        fetchVetPageData()
        fetchVetInfo()
    }


    private fun fetchVetPageData() {
        vetPageData.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getVetPageData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { registerData ->
                        vetPageData.postValue(responseHandler.handleSuccess(registerData))
                    },
                    {
                        vetPageData.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }

    private fun fetchVetInfo() {
        vetInfos.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getVetInfos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { registerData ->
                        vetInfos.postValue(responseHandler.handleSuccess(registerData))
                    },
                    {
                        vetInfos.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getVetPageData(): LiveData<Resource<PageData>> {
        return vetPageData
    }

    fun getVetInfo(): LiveData<Resource<List<VetResponse>>> {
        return vetInfos
    }


}