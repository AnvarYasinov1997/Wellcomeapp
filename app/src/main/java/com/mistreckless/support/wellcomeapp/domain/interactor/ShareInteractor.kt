package com.mistreckless.support.wellcomeapp.domain.interactor

import com.mistreckless.support.wellcomeapp.domain.entity.ShareState
import com.mistreckless.support.wellcomeapp.domain.entity.StateError
import com.mistreckless.support.wellcomeapp.domain.entity.StateInit
import com.mistreckless.support.wellcomeapp.domain.entity.StateUploaded
import com.mistreckless.support.wellcomeapp.domain.repository.LocationRepository
import com.mistreckless.support.wellcomeapp.domain.repository.PostRepository
import com.mistreckless.support.wellcomeapp.domain.repository.UserRepository
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
    fun share(addressLine: String, descLine: String, dressControl: Boolean, ageControl: Boolean, ageLine: String, fromTime: Long, tillTime: Long): Observable<ShareState>

}


class ShareInteractorImpl(private val userRepository: UserRepository,
                          private val locationRepository: LocationRepository,
                          private val postRepository: PostRepository) : ShareInteractor {
    lateinit var bytes: ByteArray

    override fun findMyLocation(): Single<String> {
        return locationRepository.getCurrentAddress()
                .map { if (it.thoroughfare != null && it.thoroughfare.isNotEmpty()) it.thoroughfare + " " + it.subThoroughfare else it.locality }
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun share(addressLine: String, descLine: String, dressControl: Boolean, ageControl: Boolean, ageLine: String, fromTime: Long, tillTime: Long): Observable<ShareState> {
        val initObservable = Observable.just(StateInit())
        val tags = findTags(descLine)
        val isAgeControl = ageControl && ageLine.isNotEmpty()
        return Observable.merge(initObservable, postRepository.uploadPost(bytes))
                .flatMap { when(it){
                    is StateUploaded-> Observable.just(it)
                    else ->Observable.just(it)
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