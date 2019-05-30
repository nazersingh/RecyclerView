package com.nazer.recyclerview.ui.activity.demo_list

import com.nazer.recyclerview.manager.callbacks.GenericCallBack
import com.nazer.recyclerview.pojo.DataPojo
import com.nazer.recyclerview.pojo.ResponsePojo
import com.nazer.recyclerview.ui.base.BaseModel
import com.nazer.recyclerview.ui.base.BaseObserver
import com.nazer.recyclerview.utility.PrintLog

class ViewModel(var callBack: BaseObserver<ArrayList<DataPojo>>):BaseModel() {

    init {
        loadJSON()
    }

    fun loadJSON() {

        callBack.addDisposal(callApi(object : GenericCallBack<ArrayList<DataPojo>> {
            override fun onSubscribe() {
                callBack.showLoader()
            }

            override fun onSuccess(t: ArrayList<DataPojo>) {
                PrintLog.e("model ",""+t)
                callBack.getApiData(t)
                callBack.hideLoader()

                setChanged()
                notifyObservers()
            }

            override fun onerror(error: Throwable) {
                callBack.hideLoader()
            }
        }))
    }
}