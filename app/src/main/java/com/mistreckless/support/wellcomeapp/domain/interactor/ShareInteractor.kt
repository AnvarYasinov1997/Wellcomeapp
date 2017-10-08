package com.mistreckless.support.wellcomeapp.domain.interactor

import com.mistreckless.support.wellcomeapp.domain.repository.LocationRepository
import com.mistreckless.support.wellcomeapp.domain.repository.PostRepository
import com.mistreckless.support.wellcomeapp.domain.repository.UserRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by @mistreckless on 08.10.2017. !
 */
interface ShareInteractor {
    fun findMyLocation(): Single<String>

}


class ShareInteractorImpl(private val userRepository: UserRepository,
                          private val locationRepository: LocationRepository,
                          private val postRepository: PostRepository) : ShareInteractor {
    override fun findMyLocation(): Single<String> {
        return locationRepository.getCurrentAddress()
                .map { if (it.thoroughfare != null && it.thoroughfare.isNotEmpty()) it.thoroughfare + " "+it.subThoroughfare else it.locality }
                .observeOn(AndroidSchedulers.mainThread())
    }

}