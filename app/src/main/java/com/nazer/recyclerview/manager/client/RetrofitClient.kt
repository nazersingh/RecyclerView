package com.nazer.recyclerview.manager.client

import com.example.igniva_android_022.designpatternmvp.utils.PreferenceHandler
import com.nazer.recyclerview.BuildConfig
import com.nazer.recyclerview.manager.application.ApplicationClass
import com.nazer.recyclerview.utility.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    var API_BASE_URL = Constants.API_BASE_URL

    private var apiInterface: ApiInterface? = null

    fun getRetrofit() : ApiInterface?
    {
        if(apiInterface==null)
            apiInterface= Retrofit.Builder().baseUrl(Constants.API_BASE_URL)
                .client(makeHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiInterface::class.java)

        return apiInterface
    }



    private fun makeHttpClient() = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(tokenIntercept)
        .addInterceptor(loggingInterceptor())
//        .addNetworkInterceptor(StethoInterceptor())
        .build()

    var tokenIntercept: Interceptor = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain?): okhttp3.Response {
            val accessToken = PreferenceHandler.readString(ApplicationClass.getApplicationInstance(), PreferenceHandler.PREF_KEY_LOGIN_TOKEN, "")
            var original: Request = chain!!.request();
            var request: Request = original.newBuilder()
                .addHeader("token", accessToken)
                .method(original.method(), original.body())
                .build();

            return chain.proceed(request);
        }
    }
//
//    fun accessTokenProvidingInterceptor() = Interceptor { chain ->
//        val accessToken = PreferenceHandler.readString(ApplicationClass.getApplicationInstance(), PreferenceHandler.PREF_KEY_LOGIN_TOKEN, "")
//        chain.proceed(chain.request().newBuilder()
//                .addHeader("token", accessToken)
//                .build())
//    }

    fun loggingInterceptor() = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }

    fun headersInterceptor() = Interceptor { chain ->
        chain.proceed(chain.request().newBuilder()

            .addHeader("Accept", "application/json")
            .addHeader("Accept-Language", "en")
            .addHeader("Content-Type", "application/json")
            .build())
    }


}