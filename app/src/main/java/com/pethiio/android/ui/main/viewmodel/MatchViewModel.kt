package com.pethiio.android.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pethiio.android.data.api.ResponseHandler
import com.pethiio.android.data.api.ServiceBuilder
import com.pethiio.android.data.model.chat.ChatListResponse
import com.pethiio.android.data.model.chat.ChatRoomResponse
import com.pethiio.android.data.model.member.MemberListResponse
import com.pethiio.android.data.model.signup.PageData
import com.pethiio.android.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MatchViewModel : ViewModel() {

    val matchPageData = MutableLiveData<Resource<PageData>>()
    private val compositeDisposable = CompositeDisposable()
    private val responseHandler: ResponseHandler = ResponseHandler()


    init {
        fetchMatchPageData()
    }


    fun fetchMatchPageData() {
        matchPageData.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getPetMatchPageData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { registerData ->
                        matchPageData.postValue(responseHandler.handleSuccess(registerData))
                    },
                    {
                        matchPageData.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getMatchPageData(): LiveData<Resource<PageData>> {
        return matchPageData
    }


}