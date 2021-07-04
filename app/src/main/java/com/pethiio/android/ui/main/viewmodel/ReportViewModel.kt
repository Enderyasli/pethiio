package com.pethiio.android.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pethiio.android.data.api.ServiceBuilder
import com.pethiio.android.data.model.petDetail.PetSearchDetailResponse
import com.pethiio.android.data.model.report.ReportRequest
import com.pethiio.android.data.model.signup.PageData
import com.pethiio.android.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import retrofit2.Response

class ReportViewModel : ViewModel() {

    private val postReportResponse = MutableLiveData<Resource<Response<Void>>>()
    private val petReportResponsePageData = MutableLiveData<Resource<PageData>>()
    private val compositeDisposable = CompositeDisposable()


    fun fetchReportPageData() {
        petReportResponsePageData.postValue(Resource.loading(null))
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

    fun postReport(reportRequest: ReportRequest) {
        postReportResponse.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .postReport(reportRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        postReportResponse.postValue(Resource.success(loginData))
                    },
                    {
                        postReportResponse.postValue(
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

    fun getReportResponse(): LiveData<Resource<Response<Void>>> {
        return postReportResponse
    }

    fun getReportPageData(): LiveData<Resource<PageData>> {
        return petReportResponsePageData
    }

}