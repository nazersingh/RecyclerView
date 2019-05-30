package com.example.igniva_android_022.designpatternmvp.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

/**
 * Common PrefrenceConnector class for storing preference values.
 *
 */
object PreferenceHandler {

    val PREF_NAME = "APPFRAMEWORK_PREFERENCES"
    val MODE = Context.MODE_PRIVATE

    val FCM_TOKEN = "FCM_TOKEN"

    val PREF_KEY_FILTER_MAX_DISTANCE = "PREF_KEY_FILTER_MAX_DISTANCE"
    val PREF_KEY_FILTER_MIN_DISTANCE = "PREF_KEY_FILTER_MIN_DISTANCE"
    val PREF_KEY_FILTER_MIN_RACESTAKE = "PREF_KEY_FILTER_MIN_RACESTAKE"
    val PREF_KEY_FILTER_MAX_RACESTAKE = "PREF_KEY_FILTER_MAX_RACESTAKE"

    val PREF_KEY_FILTER_MAX_RACESTAKE_SAVE = "PREF_KEY_FILTER_MAX_RACESTAKE_SAVE"
    val PREF_KEY_FILTER_MAX_DISTANCE_SAVE = "PREF_KEY_FILTER_MAX_DISTANCE_SAVE"
    val PREF_KEY_FILTER_MIN_DISTANCE_SAVE = "PREF_KEY_FILTER_MIN_DISTANCE_SAVE"
    val PREF_KEY_FILTER_MIN_RACESTAKE_SAVE = "PREF_KEY_FILTER_MIN_RACESTAKE_SAVE"
    val PREF_KEY_FILTER_TYPE = "PREF_KEY_FILTER_TYPE"
    val PREF_KEY_FILTER_RACE_TYPE = "PREF_KEY_FILTER_RACE_TYPE"
    val PREF_KEY_FILTER_RACE_DATE = "PREF_KEY_FILTER_RACE_DATE"


    val PREF_KEY_USER_ID = "PREF_KEY_USER_ID"
    val PREF_KEY_IS_USER_LOGIN_SOCIAL = "PREF_IS_USER_LOGIN_SOCIAL"
    val PREF_KEY_LOGIN_TOKEN = "PREF_KEY_LOGIN_TOKEN"
    val PREF_KEY_RACE_STATUS = "PREF_KEY_RACE_STATUS"
    val PREF_KEY_RACE_TOTAL = "PREF_KEY_RACE_TOTAL"

    val PREF_KEY_USER_LOGIN_TYPE = "PREF_KEY_USER_LOGIN_TYPE"

    val PREF_KEY_LOGIN_ROLE = "PREF_KEY_LOGIN_ROLE"

    val PREF_KEY_USER_NOTIFICATION_ON = "PREF_KEY_USER_NOTIFICATION_ON"

    val PREF_KEY_USER_NAME = "PREF_KEY_USER_NAME"
    val PREF_KEY_USER_EMAIL = "PREF_KEY_USER_EMAIL"
    val PREF_KEY_USER_PHONE = "PREF_KEY_USER_PHONE"
    val PREF_KEY_USER_IMAGE = "PREF_KEY_USER_IMAGE"


    fun writeBoolean(context: Context, key: String, value: Boolean) {
        getEditor(context).putBoolean(key, value).commit()
    }

    fun readBoolean(context: Context, key: String,
                    defValue: Boolean): Boolean {
        return getPreferences(context).getBoolean(key, defValue)
    }

    fun writeInteger(context: Context, key: String, value: Int) {
        getEditor(context).putInt(key, value).commit()
    }

    fun readInteger(context: Context, key: String, defValue: Int): Int {
        return getPreferences(context).getInt(key, defValue)
    }

    fun writeString(context: Context, key: String, value: String) {
        getEditor(context).putString(key, value).commit()
    }

    fun readString(context: Context, key: String, defValue: String): String? {
        return getPreferences(context).getString(key, defValue)
    }

    fun writeFCM_KEY(context: Context, key: String, value: String) {
        getFCMEditor(context).putString(key, value).commit()
    }

    fun readFCM_KEY(context: Context, key: String, defValue: String): String? {
        return getFCMPREFERENCE(context).getString(key, defValue)
    }

    fun writeFloat(context: Context, key: String, value: Float) {
        getEditor(context).putFloat(key, value).commit()
    }

    fun readFloat(context: Context, key: String, defValue: Float): Float {
        return getPreferences(context).getFloat(key, defValue)
    }

    fun writeLong(context: Context, key: String, value: Long) {
        getEditor(context).putLong(key, value).commit()
    }

    fun readLong(context: Context, key: String, defValue: Long): Long {
        return getPreferences(context).getLong(key, defValue)
    }

    fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, MODE)
    }

    fun getEditor(context: Context): Editor {
        return getPreferences(context).edit()
    }

    fun getFCMPREFERENCE(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, MODE)
    }

    fun getFCMEditor(context: Context): Editor {
        return getFCMPREFERENCE(context).edit()
    }




}
