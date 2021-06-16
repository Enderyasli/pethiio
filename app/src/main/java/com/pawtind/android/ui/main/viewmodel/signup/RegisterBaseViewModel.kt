package com.pawtind.android.ui.main.viewmodel.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pawtind.android.data.model.AccessToken
import com.pawtind.android.data.model.LookUpsResponse
import com.pawtind.android.data.model.PawtindResponse
import com.pawtind.android.data.model.signup.Login
import com.pawtind.android.data.model.signup.Register
import com.pawtind.android.data.repository.MainRepository
import com.pawtind.android.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RegisterBaseViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private val login = MutableLiveData<Resource<Login>>()
    private val register = MutableLiveData<Resource<Login>>()
    private val postRegister = MutableLiveData<Resource<AccessToken>>()
    private val fields = MutableLiveData<List<PawtindResponse>>()
    private val registerFields = MutableLiveData<List<PawtindResponse>>()
    private val registerDetailFields = MutableLiveData<List<PawtindResponse>>()
    private val registerDetailLookUps = MutableLiveData<List<LookUpsResponse>>()
    private val addAnimalLookUps = MutableLiveData<List<LookUpsResponse>>()
    private val addAnimalImageFields = MutableLiveData<List<PawtindResponse>>()
    private val addAnimalFields = MutableLiveData<List<PawtindResponse>>()
    private val compositeDisposable = CompositeDisposable()


    public fun fetchLogin() {
        login.postValue(Resource.loading(null))
        compositeDisposable.add(
            mainRepository.getLogin()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ loginData ->
                    login.postValue(Resource.success(loginData))
                    fields.postValue(loginData.fields)
                }, {
                    login.postValue(Resource.error("Something Went Wrong", null))
                })
        )
    }

    public fun fetchRegister() {
        register.postValue(Resource.loading(null))
        compositeDisposable.add(
            mainRepository.getRegister()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ registerData ->
                    register.postValue(Resource.success(registerData))
                    registerFields.postValue(registerData.fields)
                }, {
                    register.postValue(Resource.error("Something Went Wrong", null))
                })
        )
    }

    public fun fetchRegisterDetail() {
        register.postValue(Resource.loading(null))
        compositeDisposable.add(
            mainRepository.getRegisterDetail()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ registerData ->
                    register.postValue(Resource.success(registerData))
                    registerDetailFields.postValue(registerData.fields)
                    registerDetailLookUps.postValue(registerData.lookups)
                }, {
                    register.postValue(Resource.error("Something Went Wrong", null))
                })
        )
    }

    public fun fetchAnimalAddPhoto() {
        register.postValue(Resource.loading(null))
        compositeDisposable.add(
            mainRepository.getAnimalAddPhoto()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ registerData ->
                    register.postValue(Resource.success(registerData))
                    addAnimalImageFields.postValue(registerData.fields)
                }, {
                    register.postValue(Resource.error("Something Went Wrong", null))
                })
        )
    }

    public fun fetchAddAnimal() {
        register.postValue(Resource.loading(null))
        compositeDisposable.add(
            mainRepository.getAnimalAdd()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ registerData ->
                    register.postValue(Resource.success(registerData))
                    addAnimalFields.postValue(registerData.fields)
                    addAnimalLookUps.postValue(registerData.lookups)
                }, {
                    register.postValue(Resource.error("Something Went Wrong", null))
                })
        )
    }

    public fun postRegister(register: Register) {
        postRegister.postValue(Resource.loading(null))
        compositeDisposable.add(
            mainRepository.postRegister(register)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { registerData ->
                        postRegister.postValue(Resource.success(registerData))
                    },
                    {
                        postRegister.postValue(Resource.error("Something Went Wrong", null))
                    }
                )
        )
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getLogin(): LiveData<Resource<Login>> {
        return login
    }

    fun setFields(pawtindResponse: List<PawtindResponse>) {
        fields.value = pawtindResponse
    }

    fun getFields(): LiveData<List<PawtindResponse>> {
        return fields
    }

    fun getRegisterFields(): LiveData<List<PawtindResponse>> {
        return registerFields
    }

    fun getRegisterDetailFields(): LiveData<List<PawtindResponse>> {
        return registerDetailFields
    }

    fun getRegisterDetailLookUps(): LiveData<List<LookUpsResponse>> {
        return registerDetailLookUps
    }

    fun getAddImageFields(): LiveData<List<PawtindResponse>> {
        return addAnimalImageFields
    }

    fun getAddAnimalFields(): LiveData<List<PawtindResponse>> {
        return addAnimalFields
    }
    fun getAddAnimalLookUps(): LiveData<List<LookUpsResponse>> {
        return addAnimalLookUps
    }


}