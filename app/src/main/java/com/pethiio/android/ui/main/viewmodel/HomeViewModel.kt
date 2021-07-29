package com.pethiio.android.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pethiio.android.data.api.ResponseHandler
import com.pethiio.android.data.api.ServiceBuilder
import com.pethiio.android.data.model.PetListResponse
import com.pethiio.android.data.model.User
import com.pethiio.android.data.model.signup.PageData
import com.pethiio.android.data.repository.MainRepository
import com.pethiio.android.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomeViewModel : ViewModel() {

    val petList = SingleLiveEvent<Resource<List<PetListResponse>>>()
    private val homePageData = SingleLiveEvent<Resource<PageData>>()
    private val responseHandler: ResponseHandler = ResponseHandler()


    private val compositeDisposable = CompositeDisposable()

    init {
        fetchPetList()
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

    fun fetchHomePageData() {
        homePageData.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getHomePageData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        homePageData.postValue(Resource.success(loginData))
                    },
                    {
                        homePageData.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getPetList(): LiveData<Resource<List<PetListResponse>>> {
        return petList
    }

    fun getHomePageData(): LiveData<Resource<PageData>> {
        return homePageData
    }


}