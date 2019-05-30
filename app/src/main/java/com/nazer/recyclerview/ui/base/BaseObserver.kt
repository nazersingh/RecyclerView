package com.nazer.recyclerview.ui.base

import com.nazer.recyclerview.manager.application.ApplicationClass
import io.reactivex.disposables.Disposable
import java.util.*

interface BaseObserver<Any>: Observer {

    fun showLoader()
    {

    }
    fun hideLoader()
    {

    }
    fun addDisposal(disposable: Disposable)
    fun getApiData(t: Any)
}