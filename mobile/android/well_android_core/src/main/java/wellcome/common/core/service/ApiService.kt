package wellcome.common.core.service

import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET
import retrofit2.http.Query
import wellcome.common.mpp.entity.CityData
import wellcome.common.mpp.entity.UserData

actual interface ApiService {

    @GET("/api/v1/auth/initUser")
    actual fun initUser(): Deferred<UserData>

    @GET("/api/v1/auth/initCity")
    actual fun initCity(@Query("lat") lat: Double, @Query("lon") lon: Double): Deferred<CityData>

}