package com.pethiio.android.ui.main.viewmodel.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pethiio.android.data.api.ServiceBuilder
import com.pethiio.android.data.model.signup.PageData
import com.pethiio.android.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class WelcomeViewModel : ViewModel() {

    private val loginPageData = MutableLiveData<Resource<PageData>>()
    private val compositeDisposable = CompositeDisposable()

    init {
        fetchWelcome()
    }


    private fun fetchWelcome() {

        loginPageData.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getLoginPageData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        loginPageData.postValue(Resource.success(loginData))
                    },
                    {
                        loginPageData.postValue(Resource.error("Something went wrong", null))
                    }
                )
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getLogin(): LiveData<Resource<PageData>> {
        return loginPageData
    }

}