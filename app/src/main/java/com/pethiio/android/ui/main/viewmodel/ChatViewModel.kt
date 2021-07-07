package com.pethiio.android.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pethiio.android.data.api.ResponseHandler
import com.pethiio.android.data.api.ServiceBuilder
import com.pethiio.android.data.model.PetListResponse
import com.pethiio.android.data.model.User
import com.pethiio.android.data.model.chat.ChatListResponse
import com.pethiio.android.data.model.member.MemberListResponse
import com.pethiio.android.data.model.signup.PageData
import com.pethiio.android.data.repository.MainRepository
import com.pethiio.android.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class ChatViewModel() : ViewModel() {

    val chatPageData = MutableLiveData<Resource<PageData>>()
    val chatList = MutableLiveData<Resource<List<ChatListResponse>>>()
    private val memberList = MutableLiveData<Resource<List<MemberListResponse>>>()

    private val compositeDisposable = CompositeDisposable()
    private val responseHandler: ResponseHandler = ResponseHandler()


    init {
        fetchChatPageData()
    }


    fun fetchChatPageData() {
        chatPageData.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getChatPageData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { registerData ->
                        chatPageData.postValue(responseHandler.handleSuccess(registerData))
                    },
                    {
                        chatPageData.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }

    fun fetchChatList(memberId: Int) {
        chatList.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getChatList(memberId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { registerData ->
                        chatList.postValue(responseHandler.handleSuccess(registerData))
                    },
                    {
                        chatList.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }

    fun fetchMemberList() {

        memberList.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getMemberList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        memberList.postValue(Resource.success(loginData))
                    },
                    {
                        memberList.postValue(responseHandler.handleException(it))

                    }
                )
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getChatPageData(): LiveData<Resource<PageData>> {
        return chatPageData
    }

    fun getMemberList(): LiveData<Resource<List<MemberListResponse>>> {
        return memberList
    }
    fun getChatList(): LiveData<Resource<List<ChatListResponse>>> {
        return chatList
    }

}