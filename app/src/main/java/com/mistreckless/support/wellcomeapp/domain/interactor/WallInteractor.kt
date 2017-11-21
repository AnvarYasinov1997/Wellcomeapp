package com.mistreckless.support.wellcomeapp.domain.interactor

import com.google.firebase.firestore.DocumentReference
import com.mistreckless.support.wellcomeapp.data.repository.LocationRepository
import com.mistreckless.support.wellcomeapp.data.repository.PostRepository
import com.mistreckless.support.wellcomeapp.data.repository.UserRepository
import io.reactivex.Observable

/**
 * Created by mistreckless on 19.10.17.
 */

interface WallInteractor {
    fun controlWall(observeScroll: Observable<Int>) : Observable<List<DocumentReference>>
}


class WallInteractorImpl(private val userRepository : UserRepository, private val postRepository: PostRepository, private val locationRepository: LocationRepository) : WallInteractor{
    override fun controlWall(observeScroll: Observable<Int>): Observable<List<DocumentReference>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}