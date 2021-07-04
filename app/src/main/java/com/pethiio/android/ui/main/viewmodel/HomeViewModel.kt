package com.pethiio.android.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pethiio.android.data.api.ServiceBuilder
import com.pethiio.android.data.model.PetListResponse
import com.pethiio.android.data.model.User
import com.pethiio.android.data.repository.MainRepository
import com.pethiio.android.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomeViewModel() : ViewModel() {

    val petList = MutableLiveData<Resource<List<PetListResponse>>>()

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
                        petList.postValue(Resource.error(it.message, null))
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


}