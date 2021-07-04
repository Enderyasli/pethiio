package com.pethiio.android.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pethiio.android.data.api.ServiceBuilder
import com.pethiio.android.data.model.petDetail.PetSearchDetailResponse
import com.pethiio.android.data.model.petDetail.PetSearchOwnerDetailResponse
import com.pethiio.android.data.model.signup.PageData
import com.pethiio.android.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class PetDetailViewModel : ViewModel() {

    private val petDetailResponse = MutableLiveData<Resource<PetSearchDetailResponse>>()
    private val petOwnerDetailResponse = MutableLiveData<Resource<PetSearchOwnerDetailResponse>>()
    private val petDetailResponsePageData = MutableLiveData<Resource<PageData>>()
    private val compositeDisposable = CompositeDisposable()


    fun fetchPetDetail() {

        petDetailResponse.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getPetDetail( "62",382)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        petDetailResponse.postValue(Resource.success(loginData))
                    },
                    {
                        if ((it as HttpException).code() == 403)
                            petDetailResponse.postValue(Resource.error("Something went wrong", 403))
                    }
                )
        )
    }

    fun fetchPetOwnerDetail(userId:String) {

        petDetailResponse.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getPetOwnerDetail( userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        petOwnerDetailResponse.postValue(Resource.success(loginData))
                    },
                    {
                        if ((it as HttpException).code() == 403)
                            petOwnerDetailResponse.postValue(Resource.error("Something went wrong", 403))
                    }
                )
        )
    }

    fun fetchPetDetailPageData() {

        petDetailResponse.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getPetSearchDetailPageData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        petDetailResponsePageData.postValue(Resource.success(loginData))
                    },
                    {
                            petDetailResponsePageData.postValue(Resource.error("Something went wrong", 403))
                    }
                )
        )
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getPetDetail(): LiveData<Resource<PetSearchDetailResponse>> {
        return petDetailResponse
    }

    fun getPetOwnerDetail(): LiveData<Resource<PetSearchOwnerDetailResponse>> {
        return petOwnerDetailResponse
    }
    fun getPetDetailPageData(): LiveData<Resource<PageData>> {
        return petDetailResponsePageData
    }

}