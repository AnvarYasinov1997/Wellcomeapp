package wellcome.common.core.service

import kotlinx.coroutines.experimental.Deferred
import wellcome.common.mpp.entity.CityData
import wellcome.common.mpp.entity.UserData

expect interface ApiService {
    fun initUser(): Deferred<UserData>
    fun initCity(lat: Double, lon: Double): Deferred<CityData>

}