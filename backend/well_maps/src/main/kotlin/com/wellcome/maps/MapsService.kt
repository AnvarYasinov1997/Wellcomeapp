package com.wellcome.maps

import com.google.maps.GeoApiContext
import com.wellcome.configuration.message.FindLocalityMessage
import com.wellcome.configuration.message.LocalityFoundMessage
import com.wellcome.configuration.message.LocalityNotFoundMessage
import com.wellcome.configuration.message.MapsReturnMessage
import com.wellcome.configuration.utils.LoggerHandler
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async

interface MapsService {
    fun findLocality(data: FindLocalityMessage): Deferred<MapsReturnMessage>
}

class GoogleMapsService(private val geoApiContext: GeoApiContext,
                        private val logger: LoggerHandler) : MapsService {
    override fun findLocality(data: FindLocalityMessage): Deferred<MapsReturnMessage> = async {
        val localityJob = geoApiContext.getLocality(data.lat, data.lon)
        val timezoneJob = geoApiContext.getTimezoneId(data.lat, data.lon)
        val localityName = localityJob.await()
        val timezoneId = timezoneJob.await()

        logger.info("locality $localityName timezone $timezoneId geopoints ${data.lat} ${data.lon}")

        return@async if (localityName.isEmpty() || timezoneId.isEmpty())
            LocalityNotFoundMessage("locality $localityName timezone $timezoneId")
        else LocalityFoundMessage(localityName, timezoneId)

    }

}