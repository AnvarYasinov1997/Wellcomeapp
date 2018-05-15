package com.wellcome.core

import android.content.SharedPreferences


class Cache(private val preferences: SharedPreferences) {

     fun cacheString(key: String, value: String) =
        preferences.edit().putString(key, value).apply()

     fun cacheInt(key: String, value: Int) = preferences.edit().putInt(key, value).apply()
     fun cacheLong(key: String, value: Long) = preferences.edit().putLong(key, value).apply()
     fun cacheDouble(key: String, value: Double) =
        preferences.edit().putString(key, value.toString()).apply()

     fun getString(key: String, defaultValue: String): String =
        preferences.getString(key, defaultValue)

     fun getInt(key: String, defaultValue: Int): Int = preferences.getInt(key, defaultValue)
     fun getLong(key: String, defaultValue: Long): Long =
        preferences.getLong(key, defaultValue)

     fun getDouble(key: String, defaultValue: Double): Double =
        preferences.getString(key, defaultValue.toString()).toDouble()
}