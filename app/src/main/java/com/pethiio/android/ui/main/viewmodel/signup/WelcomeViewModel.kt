package com.pethiio.android.ui.main.viewmodel.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pethiio.android.data.model.signup.Login
import com.pethiio.android.data.repository.MainRepository
import com.pethiio.android.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class WelcomeViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private val users = MutableLiveData<Resource<Login>>()
    private val compositeDisposable = CompositeDisposable()

    init {
        fetchWelcome()
    }


    private fun fetchWelcome() {
        users.postValue(Resource.loading(null))
        compositeDisposable.add(
            mainRepository.getLogin()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ userList ->
                    users.postValue(Resource.success(userList))
                }, {
                    users.postValue(Resource.error("Something Went Wrong", null))
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getLogin(): LiveData<Resource<Login>> {
        return users
    }

}