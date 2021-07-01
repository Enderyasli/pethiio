package com.pethiio.android.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pethiio.android.data.api.ServiceBuilder
import com.pethiio.android.data.model.member.LocationsRequest
import com.pethiio.android.data.model.member.MemberListResponse
import com.pethiio.android.data.model.member.PetSearchResponse
import com.pethiio.android.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class DashBoardViewModel : ViewModel() {

    private val locations = MutableLiveData<Resource<Response<Void>>>()
    private val memberList = MutableLiveData<Resource<List<MemberListResponse>>>()
    private val petSearchResult = MutableLiveData<Resource<List<PetSearchResponse>>>()
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
                        memberList.postValue(Resource.error("Something went wrong", null))
                    }
                )
        )
    }

    fun fetchPetSearch(animalId:Int) {

        memberList.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getPetSearch(animalId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        petSearchResult.postValue(Resource.success(loginData))
                    },
                    {
                        petSearchResult.postValue(Resource.error(it.message, null))
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

    fun getMemberList(): LiveData<Resource<List<MemberListResponse>>> {
        return memberList
    }
    fun getSearchList(): LiveData<Resource<List<PetSearchResponse>>> {
        return petSearchResult
    }

}