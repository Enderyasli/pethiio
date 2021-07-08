package com.pethiio.android.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pethiio.android.data.api.ResponseHandler
import com.pethiio.android.data.api.ServiceBuilder
import com.pethiio.android.data.model.LookUpsResponse
import com.pethiio.android.data.model.PethiioResponse
import com.pethiio.android.data.model.filter.PetSearchFilterResponse
import com.pethiio.android.data.model.filter.SearchFilterRequest
import com.pethiio.android.data.model.member.LocationsRequest
import com.pethiio.android.data.model.member.MemberListResponse
import com.pethiio.android.data.model.member.PetSearchRequest
import com.pethiio.android.data.model.member.PetSearchResponse
import com.pethiio.android.data.model.signup.PageData
import com.pethiio.android.utils.CommonFunctions
import com.pethiio.android.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import retrofit2.Response

class DashBoardViewModel : ViewModel() {

    private val petSearchPageData = MutableLiveData<Resource<PageData>>()

    private val locations = MutableLiveData<Resource<Response<Void>>>()
    private val locationPageData = MutableLiveData<Resource<PageData>>()
    private val memberList = MutableLiveData<Resource<List<MemberListResponse>>>()
    private val petSearchResult = MutableLiveData<Resource<List<PetSearchResponse>>>()
    private val postPetSearchResult = MutableLiveData<Resource<Response<Void>>>()
    private val postSearchFilter = MutableLiveData<Resource<Response<Void>>>()
    private val petSearchFilter = MutableLiveData<Resource<PetSearchFilterResponse>>()
    private val petSearchFilterPageData = MutableLiveData<Resource<List<PethiioResponse>>>()
    private val petSearchFilterPageDataLookUps = MutableLiveData<Resource<List<LookUpsResponse>>>()
    private val responseHandler: ResponseHandler = ResponseHandler()


    private val compositeDisposable = CompositeDisposable()

    fun fetchPetSearchPageData() {
        petSearchPageData.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getPetSearchPageData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        petSearchPageData.postValue(Resource.success(loginData))
                    },
                    {
                        petSearchPageData.postValue(
                            responseHandler.handleException(it)
                        )
                    }
                )
        )
    }

    fun fetchLocationPageData() {
        locationPageData.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getLocationAccessPageData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        locationPageData.postValue(Resource.success(loginData))
                    },
                    {
                        locationPageData.postValue(
                            responseHandler.handleException(it)
                        )
                    }
                )
        )
    }

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
                        locations.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }
    // TODO: 8.07.2021 responsehandler

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

    fun fetchSearchFilterListPageData() {

        petSearchFilterPageData.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getPetSearchListPageData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        petSearchFilterPageData.postValue(Resource.success(loginData.fields))
                        petSearchFilterPageDataLookUps.postValue(Resource.success(loginData.lookups))
                    },
                    {
                        petSearchFilterPageData.postValue(
                            responseHandler.handleException(it)
                        )
                    }
                )
        )
    }

    fun fetchFilterList() {

        petSearchFilter.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getPetSearchList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        petSearchFilter.postValue(Resource.success(loginData))
                    },
                    {
                        petSearchFilter.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }

    fun fetchPetSearch(animalId: Int) {

        petSearchResult.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .getPetSearch(animalId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { loginData ->
                        petSearchResult.postValue(Resource.success(loginData.content))
                    },
                    {
                        petSearchResult.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }

    fun postPetSearch(petSearchRequest: PetSearchRequest) {

        postPetSearchResult.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .postPetSearch(petSearchRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        if (it.code().toString().startsWith("2"))
                            postPetSearchResult.postValue(Resource.success(null))
                        else
                            postPetSearchResult.postValue(Resource.error(it.message(), null))
                    },
                    {
                        postPetSearchResult.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }

    fun postSearhFilter(filterRequest: SearchFilterRequest) {

        postSearchFilter.postValue(Resource.loading(null))
        compositeDisposable.add(
            ServiceBuilder.buildService()
                .postSearchFilter(filterRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        if (it.code().toString().startsWith("2"))
                            postSearchFilter.postValue(Resource.success(null))
                        else
                            postSearchFilter.postValue(Resource.error(it.message(), null))
                    },
                    {
                        postSearchFilter.postValue(responseHandler.handleException(it))
                    }
                )
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getPetSearchPageData(): LiveData<Resource<PageData>> {
        return petSearchPageData
    }
    fun getLocationPageData(): LiveData<Resource<PageData>> {
        return locationPageData
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

    fun postSearchResponse(): LiveData<Resource<Response<Void>>> {
        return postPetSearchResult
    }

    fun getSearchFilterList(): LiveData<Resource<PetSearchFilterResponse>> {
        return petSearchFilter
    }

    fun getSearchFilterListFields(): LiveData<Resource<List<PethiioResponse>>> {
        return petSearchFilterPageData
    }

    fun getSearchFilterListLookUps(): LiveData<Resource<List<LookUpsResponse>>> {
        return petSearchFilterPageDataLookUps
    }

    fun getPostSearchFilter(): LiveData<Resource<Response<Void>>> {
        return postSearchFilter
    }


}