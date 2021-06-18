package com.pawtind.android.data.api

import com.pawtind.android.data.model.AccessToken
import com.pawtind.android.data.model.signup.Login
import com.pawtind.android.data.model.signup.Register
import com.rx2androidnetworking.Rx2AndroidNetworking
import io.reactivex.Observable
import io.reactivex.Single

class ApiServiceImpl : ApiService {


    override fun getLoginPageData(): Single<Login> {
        return Rx2AndroidNetworking.get("http://api.pawtind.com/api/page/login/info")
            .build()
            .getObjectSingle(Login::class.java)
    }

    override fun getRegisterPageData(): Single<Login> {
        return Rx2AndroidNetworking.get("http://api.pawtind.com/api/page/register/info")
            .build()
            .getObjectSingle(Login::class.java)
    }

    override fun getRegisterDetailPageData(): Single<Login> {
        return Rx2AndroidNetworking.get("http://api.pawtind.com/api/page/register-detail/info")
            .build()
            .getObjectSingle(Login::class.java)
    }

    override fun getAnimalAddPhotoPageData(): Single<Login> {
        return Rx2AndroidNetworking.get("http://api.pawtind.com/api/page/animal-add-photo/info")
            .build()
            .getObjectSingle(Login::class.java)
    }

    override fun getAnimalAddPageData(): Single<Login> {
        return Rx2AndroidNetworking.get("http://api.pawtind.com/api/page/pet-add/info")
            .build()
            .getObjectSingle(Login::class.java)
    }

    override fun postRegister(register: Register): Single<AccessToken> {
        return Rx2AndroidNetworking.post("http://api.pawtind.com/api/page/register")
            .addApplicationJsonBody(register)
            .addHeaders("Content-Type", "application/json")
            .build()
            .getObjectSingle(AccessToken::class.java)
    }


}