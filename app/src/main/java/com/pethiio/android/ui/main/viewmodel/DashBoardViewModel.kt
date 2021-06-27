package com.pethiio.android.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pethiio.android.data.api.ServiceBuilder
import com.pethiio.android.data.model.LocationsRequest
import com.pethiio.android.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class DashBoardViewModel : ViewModel() {

    private val locations = MutableLiveData<Resource<Response<Void>>>()
    private val compositeDisposable = CompositeDisposable()


    fun fetchLocations(locationRequest: LocationsRequest) {

        locations.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .postLocations(locationRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        if (loginData.code().toString().startsWith("2"))
                            locations.postValue(Resource.success(loginData))
                    },
                    {
                        locations.postValue(Resource.error("Something went wrong", null))
                    }
                )
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getLocations(): LiveData<Resource<Response<Void>>> {
        return locations
    }

}