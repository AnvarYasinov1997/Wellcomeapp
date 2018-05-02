package wellcome.common.repository

import wellcome.common.cache.Cache
import wellcome.common.cache.CacheConst

class UserRepository(private val cache: Cache) {
    fun getUserReference(): String = cache.getString(CacheConst.USER_REF, "")
}