package com.mistreckless.support.wellcomeapp.domain

import android.content.Context
import com.google.android.gms.location.LocationRequest
import com.ironz.binaryprefs.BinaryPreferencesBuilder
import com.ironz.binaryprefs.Preferences
import com.mistreckless.support.wellcomeapp.data.CacheData
import com.mistreckless.support.wellcomeapp.data.CacheDataImp
import com.mistreckless.support.wellcomeapp.domain.interactor.*
import com.mistreckless.support.wellcomeapp.data.repository.*
import com.mistreckless.support.wellcomeapp.data.rxlocation.RxLocation
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by @mistreckless on 31.07.2017. !
 */



@Singleton
@Module
class InteractorModule {

    @Singleton
    @Provides
    fun provideMainInteractor(userRepository: UserRepository): MainInteractor = MainInteractorImpl(userRepository)

    @Singleton
    @Provides
    fun provideRegistryInteractor(userRepository: UserRepository, locationRepository : LocationRepository) : RegistryInteractor = RegistryInteractorImpl(userRepository, locationRepository)

    @Singleton
    @Provides
    fun provideDataInteractor(userRepository: UserRepository) : DataInteractor = DataInteractorImpl(userRepository)

    @Singleton
    @Provides
    fun provideProfileInteractor(dataInteractor: DataInteractor,userRepository: UserRepository) : ProfileInteractor = ProfileInteractorImpl(dataInteractor, userRepository)

    @Singleton
    @Provides
    fun provideShareInteractor(userRepository: UserRepository,locationRepository: LocationRepository,postRepository: PostRepository) : ShareInteractor = ShareInteractorImpl(userRepository, locationRepository, postRepository)

    @Singleton
    @Provides
    fun provideWallInteractor(userRepository: UserRepository,postRepository: PostRepository,locationRepository: LocationRepository) : WallInteractor = WallInteractorImpl(userRepository, postRepository, locationRepository)

}
