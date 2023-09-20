package com.aait.getak.network.remote_db


class Resource<T> private constructor(val status: Status, val data: T?,
                                      val message: String?, val is_first: Boolean?) {

    enum class Status {
        SUCCESS, ERROR, LOADING,LOADING_FIRST,IS_FINISH_LOAD
    }

    companion object {

        fun <T> success(data: T,is_first: Boolean?=true): Resource<T> {
            return Resource(Status.SUCCESS, data,null,is_first)
        }

        fun <T> error(msg: String, data: T?,is_first: Boolean?=true): Resource<T> {
            return Resource(Status.ERROR, data, msg,is_first)
        }

        fun <T> loading(): Resource<T> {
            return Resource(Status.LOADING_FIRST, null, null,is_first = true)
        }
        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING,null, null,is_first = false)
        }

    }
}