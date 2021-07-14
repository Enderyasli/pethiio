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

class MainActivityViewModel : ViewModel() {

    val allChatList = MutableLiveData<Resource<List<ChatListResponse>>>()

    private val compositeDisposable = CompositeDisposable()
    private val responseHandler: ResponseHandler = ResponseHandler()


    init {
        fetchChatList()
    }


    fun fetchChatList() {
        allChatList.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getAllChatList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { registerData ->
                        allChatList.postValue(responseHandler.handleSuccess(registerData))
                    },
                    {
                        allChatList.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getAllChatList(): LiveData<Resource<List<ChatListResponse>>> {
        return allChatList
    }


}