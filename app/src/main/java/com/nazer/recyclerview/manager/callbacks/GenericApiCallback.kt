package com.nazer.recyclerview.manager.callbacks

interface GenericApiCallback<T> {
    fun onSuccess(t:T)
    fun onerror(error:Throwable)
}