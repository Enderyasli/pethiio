package com.pethiio.android.utils

data class Resource<out T>(
    val status: Status,
    val data: T?,
    val message: String?,
    val errorCode: Int?
) {

    companion object {

        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null, null)
        }

        fun <T> error(msg: String?, errorCode: Int?): Resource<T> {
            return Resource(Status.ERROR, null, msg, errorCode)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null, null)
        }

    }

}