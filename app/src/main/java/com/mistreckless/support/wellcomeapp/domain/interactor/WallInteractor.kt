package com.mistreckless.support.wellcomeapp.domain.interactor

import com.mistreckless.support.wellcomeapp.domain.repository.LocationRepository
import com.mistreckless.support.wellcomeapp.domain.repository.PostRepository
import com.mistreckless.support.wellcomeapp.domain.repository.UserRepository

/**
 * Created by mistreckless on 19.10.17.
 */

interface WallInteractor


class WallInteractorImpl(private val userRepository : UserRepository, private val postRepository: PostRepository, private val locationRepository: LocationRepository) : WallInteractor{

}