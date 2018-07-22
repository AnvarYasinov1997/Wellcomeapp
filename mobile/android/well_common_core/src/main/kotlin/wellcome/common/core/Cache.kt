package wellcome.common.core

expect class Cache {
    fun cacheString(key: String, value: String)
    fun cacheInt(key: String, value: Int)
    fun cacheLong(key: String, value: Long)
    fun cacheDouble(key: String, value: Double)
    fun getString(key: String, defaultValue: String): String
    fun getInt(key: String, defaultValue: Int): Int
    fun getLong(key: String, defaultValue: Long): Long
    fun getDouble(key: String, defaultValue: Double): Double
}