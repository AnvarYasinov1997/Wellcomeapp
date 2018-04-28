package com.mistreckless.support.wellcomeapp.data

import com.ironz.binaryprefs.Preferences
import com.ironz.binaryprefs.serialization.serializer.persistable.Persistable

/**
 * Created by @mistreckless on 26.08.2017. !
 */

class CacheDataImp(private val preferences: Preferences) : CacheData {

    override fun cachePersistable(key: String, value: Persistable) =
        preferences.edit().putPersistable(key, value).apply()

    override fun cacheString(key: String, value: String) =
        preferences.edit().putString(key, value).apply()

    override fun cacheInt(key: String, value: Int) = preferences.edit().putInt(key, value).apply()

    override fun cacheLong(key: String, value: Long) =
        preferences.edit().putLong(key, value).apply()

    override fun cacheDouble(key: String, value: Double) =
        preferences.edit().putDouble(key, value).apply()

    override fun <T : Persistable> getPersistable(key: String): T? =
        preferences.getPersistable(key, null)

    override fun getString(key: String, defaultValue: String): String =
        preferences.getString(key, defaultValue)

    override fun getInt(key: String, defaultValue: Int): Int = preferences.getInt(key, defaultValue)

    override fun getLong(key: String, defaultValue: Long): Long =
        preferences.getLong(key, defaultValue)

    override fun getDouble(key: String, defaultValue: Double): Double =
        preferences.getDouble(key, defaultValue)
}

interface CacheData {
    fun cachePersistable(key: String, value: Persistable)

    fun cacheString(key: String, value: String)

    fun cacheInt(key: String, value: Int)

    fun cacheLong(key: String, value: Long)

    fun cacheDouble(key: String, value: Double)

    fun <T : Persistable> getPersistable(key: String): T?

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