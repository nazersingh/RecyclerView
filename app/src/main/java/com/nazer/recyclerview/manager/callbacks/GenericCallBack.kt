package com.nazer.recyclerview.manager.callbacks

interface GenericCallBack<T> {
    fun onSuccess(t:T)
    fun onerror(error:Throwable)
    fun onSubscribe()
}