package wellcome.common.cache

import com.ironz.binaryprefs.Preferences

actual class Cache(private val preferences: Preferences) {
    actual fun cacheString(key: String, value: String) =
        preferences.edit().putString(key, value).apply()

    actual fun cacheInt(key: String, value: Int) = preferences.edit().putInt(key, value).apply()
    actual fun cacheLong(key: String, value: Long) = preferences.edit().putLong(key, value).apply()
    actual fun cacheDouble(key: String, value: Double) =
        preferences.edit().putDouble(key, value).apply()

    actual fun getString(key: String, defaultValue: String): String =
        preferences.getString(key, defaultValue)

    actual fun getInt(key: String, defaultValue: Int): Int = preferences.getInt(key, defaultValue)
    actual fun getLong(key: String, defaultValue: Long): Long =
        preferences.getLong(key, defaultValue)

    actual fun getDouble(key: String, defaultValue: Double): Double =
        preferences.getDouble(key, defaultValue)
}