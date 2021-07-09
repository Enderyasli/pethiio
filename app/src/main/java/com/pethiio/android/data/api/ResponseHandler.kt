package com.pethiio.android.data.api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pethiio.android.data.model.error.PethiioErrorHandler
import com.pethiio.android.utils.Resource
import retrofit2.HttpException
import java.net.SocketTimeoutException

enum class ErrorCodes(val code: Int) {
    SocketTimeOut(-1),
    UnAuthorized(403)
}

open class ResponseHandler {
    fun <T : Any> handleSuccess(data: T): Resource<T> {
        return Resource.success(data)
    }


    fun <T : Any> handleException(e: Throwable): Resource<T> {


        return when (e) {

            is HttpException -> {

//                if (ErrorCodes.UnAuthorized.code != e.code()) {
//                    Resource.error(
//                        getErrorMessage(ErrorCodes.UnAuthorized.code),
//                        null
//                    )
//                } else {
                    val body = e.response()?.errorBody()
                    val gson = Gson()
                    val type = object : TypeToken<PethiioErrorHandler>() {}.type
                    val errorResponse: PethiioErrorHandler? =
                        gson.fromJson(body?.charStream(), type)
                    Resource.error(errorResponse?.apierror?.message, null)
//                }
            }
            is SocketTimeoutException -> Resource.error(
                getErrorMessage(ErrorCodes.SocketTimeOut.code),
                null
            )
            else -> Resource.error(getErrorMessage(Int.MAX_VALUE), null)
        }
    }

    private fun getErrorMessage(code: Int): String {
        return when (code) {
            ErrorCodes.SocketTimeOut.code -> "Timeout"
            ErrorCodes.UnAuthorized.code -> "Unauthorized"
            404 -> "Not found"
            else -> "Something went wrong"
        }
    }
}