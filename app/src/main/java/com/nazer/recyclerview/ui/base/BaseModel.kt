package com.nazer.recyclerview.ui.base

import android.util.Log
import com.nazer.recyclerview.manager.callbacks.GenericCallBack
import com.nazer.recyclerview.manager.client.RetrofitClient
import com.nazer.recyclerview.pojo.DataPojo
import com.nazer.recyclerview.pojo.ResponsePojo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

abstract class BaseModel: Observable(){
    /**
     * call Login
     */
    fun callLoginApi(hashMap: HashMap<Any, Any>, genericCallBack: GenericCallBack<ResponsePojo>): Disposable {

        return RetrofitClient.getRetrofit()?.login(hashMap)!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                Log.d("Subscribe ", it.toString())
                genericCallBack.onSubscribe()
            }
            .doOnTerminate { Log.d("OnTerminate", " terminate") }
            .subscribe({ genericCallBack.onSuccess(it) }, {
                genericCallBack.onerror(it)
            })
    }


    fun callApi(genericCallBack: GenericCallBack<ArrayList<DataPojo>>): Disposable {

        return RetrofitClient.getRetrofit()?.getData()!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                Log.d("Subscribe ", it.toString())
                genericCallBack.onSubscribe()
            }
            .doOnTerminate {
                Log.d("OnTerminate", " terminate") }
            .subscribe(
                {
                    genericCallBack.onSuccess(it) },
                {
                genericCallBack.onerror(it) }
            )
    }
}