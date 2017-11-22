package com.mistreckless.support.wellcomeapp.domain.interactor

import com.mistreckless.support.wellcomeapp.data.repository.EventRepository
import com.mistreckless.support.wellcomeapp.data.repository.LocationRepository
import com.mistreckless.support.wellcomeapp.data.repository.UserRepository
import com.mistreckless.support.wellcomeapp.domain.entity.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by @mistreckless on 08.10.2017. !
 */
interface ShareInteractor {
    fun findMyLocation(): Single<String>
    fun putPhotoBytes(bytes: ByteArray)
    fun getPhotoBytes(): ByteArray
    fun share(addressLine: String, descLine: String,fromTime: Long, tillTime: Long): Observable<ShareState>

}


class ShareInteractorImpl(private val userRepository: UserRepository,
                          private val locationRepository: LocationRepository,
                          private val eventRepository: EventRepository) : ShareInteractor {
    lateinit var bytes: ByteArray

    override fun findMyLocation(): Single<String> {
        return locationRepository.getCurrentAddress()
                .map { if (it.thoroughfare != null && it.thoroughfare.isNotEmpty()) it.thoroughfare + " " + it.subThoroughfare else it.locality }
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun share(addressLine: String, descLine: String, fromTime: Long, tillTime: Long): Observable<ShareState> {
        val initObservable = Observable.just(StateInit())
        val tags = findTags(descLine)
        return Observable.merge(initObservable, eventRepository.uploadEvent(bytes))
                .flatMap { state-> when(state){
                    is StateUploaded-> eventRepository.share(ContentType.PHOTO,state.url,userRepository.getUserReference(),addressLine,descLine,fromTime,tillTime,tags)
                    else ->Observable.just(state)
                } }
                .onErrorReturn { StateError(it.message ?: "Empty error") }
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun putPhotoBytes(bytes: ByteArray) {
        this.bytes = bytes
    }

    override fun getPhotoBytes() = bytes

    private fun findTags(text: String): List<String> {
        return text.split(" ".toRegex()).filter { it.isNotEmpty() && it[0] == '#' }
    }

}