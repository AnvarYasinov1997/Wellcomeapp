package wellcome.common.repository

import com.wellcome.core.Cache
import wellcome.common.core.CacheConst

class UserRepository(private val cache: Cache) {
    fun getUserReference(): String = cache.getString(CacheConst.USER_REF, "")
}