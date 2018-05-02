package com.mistreckless.support.wellcomeapp.data

import android.content.SharedPreferences


/**
 * Created by @mistreckless on 26.08.2017. !
 */

class CacheDataImp(private val preferences: SharedPreferences) : CacheData {



    override fun cacheString(key: String, value: String) =
        preferences.edit().putString(key, value).apply()

    override fun cacheInt(key: String, value: Int) = preferences.edit().putInt(key, value).apply()

    override fun cacheLong(key: String, value: Long) =
        preferences.edit().putLong(key, value).apply()

    override fun cacheDouble(key: String, value: Double) =
        preferences.edit().putString(key, value.toString()).apply()

    override fun getString(key: String, defaultValue: String): String =
        preferences.getString(key, defaultValue)

    override fun getInt(key: String, defaultValue: Int): Int = preferences.getInt(key, defaultValue)

    override fun getLong(key: String, defaultValue: Long): Long =
        preferences.getLong(key, defaultValue)

    override fun getDouble(key: String, defaultValue: Double): Double =
        preferences.getString(key, defaultValue.toString()).toDouble()
}

interface CacheData {

    fun cacheString(key: String, value: String)

    fun cacheInt(key: String, value: Int)

    fun cacheLong(key: String, value: Long)

    fun cacheDouble(key: String, value: Double)

    fun getString(key: String, defaultValue: String = ""): String

    fun getInt(key: String, defaultValue: Int = 0): Int

    fun getLong(key: String, defaultValue: Long = 0): Long

    fun getDouble(key: String, defaultValue: Double = 0.toDouble()): Double

    companion object {
        const val USER_REF = "user_ref"
        const val USER_CITY = "user_city"
        const val USER_CITY_REF = "user_city_ref"
        const val USER_ID = "user_id"
        const val USER_NAME = "user_displayed_name"
        const val USER_PHOTO = "user_photo"
        const val TMP_LAT = "tmp_lat"
        const val TMP_LON = "tmp_lon"
    }
}