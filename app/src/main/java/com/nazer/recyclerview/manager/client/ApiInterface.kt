package com.nazer.recyclerview.manager.client

import com.nazer.recyclerview.pojo.DataPojo
import com.nazer.recyclerview.pojo.ResponsePojo
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {

//    @GET("stations.json")
//    fun getData() : Observable<ResponsePojo>
//
    @GET("orgs")
    fun getData() : Observable<ArrayList<DataPojo>>

    @POST("people/")
    fun login(@Body hashMap: HashMap<Any,Any>) : Observable<ResponsePojo>
}