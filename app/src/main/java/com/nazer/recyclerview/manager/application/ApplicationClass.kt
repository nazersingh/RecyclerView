package com.nazer.recyclerview.manager.application

import android.app.Application


public class ApplicationClass : Application() {


    val TAG: String = ApplicationClass::class.java.simpleName



    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {

        private var instance: ApplicationClass? = null

        fun getApplicationInstance(): ApplicationClass {
            return instance!!
        }

    }
}
