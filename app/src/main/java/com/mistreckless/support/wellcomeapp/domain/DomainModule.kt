package com.mistreckless.support.wellcomeapp.domain

import com.mistreckless.support.wellcomeapp.data.repository.LocationRepository
import com.mistreckless.support.wellcomeapp.data.repository.EventRepository
import com.mistreckless.support.wellcomeapp.data.repository.UserRepository
import com.mistreckless.support.wellcomeapp.domain.interactor.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Module
class InteractorModule {

    @Singleton
    @Provides
    fun provideDataInteractor(userRepository: UserRepository): DataInteractor =
        DataInteractorImpl(userRepository)

    @Singleton
    @Provides
    fun provideProfileInteractor(
        dataInteractor: DataInteractor,
        userRepository: UserRepository
    ): ProfileInteractor = ProfileInteractorImpl(dataInteractor, userRepository)

    @Singleton
    @Provides
    fun provideShareInteractor(
        userRepository: UserRepository,
        locationRepository: LocationRepository,
        eventRepository: EventRepository
    ): ShareInteractor = ShareInteractorImpl(userRepository, locationRepository, eventRepository)

    @Singleton
    @Provides
    fun provideWallInteractor(
        userRepository: UserRepository,
        eventRepository: EventRepository,
        locationRepository: LocationRepository
    ): EventInteractor = EventInteractorImpl(userRepository, eventRepository, locationRepository)

}
