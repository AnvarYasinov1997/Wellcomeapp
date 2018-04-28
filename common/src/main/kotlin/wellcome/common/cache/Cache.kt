package wellcome.common.cache

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

object CacheConst {
    const val USER_REF = "user_ref"
    const val USER_CITY = "user_city"
    const val USER_CITY_REF = "user_city_ref"
    const val USER_ID = "user_id"
    const val USER_NAME = "user_displayed_name"
    const val USER_PHOTO = "user_photo"
    const val TMP_LAT = "tmp_lat"
    const val TMP_LON = "tmp_lon"
}