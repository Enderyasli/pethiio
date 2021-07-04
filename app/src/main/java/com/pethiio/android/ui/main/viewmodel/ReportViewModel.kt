package com.pethiio.android.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pethiio.android.data.api.ServiceBuilder
import com.pethiio.android.data.model.petDetail.PetSearchDetailResponse
import com.pethiio.android.data.model.signup.PageData
import com.pethiio.android.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class ReportViewModel : ViewModel() {

    private val petDetailResponse = MutableLiveData<Resource<PetSearchDetailResponse>>()
    private val petReportResponsePageData = MutableLiveData<Resource<PageData>>()
    private val compositeDisposable = CompositeDisposable()


    fun fetchReportPageData() {
        petDetailResponse.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getReportPageData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        petReportResponsePageData.postValue(Resource.success(loginData))
                    },
                    {
                        petReportResponsePageData.postValue(
                            Resource.error(
                                "Something went wrong",
                                null
                            )
                        )
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

    fun getReportPageData(): LiveData<Resource<PageData>> {
        return petReportResponsePageData
    }

}