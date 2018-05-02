package com.mistreckless.support.wellcomeapp.domain

import com.mistreckless.support.wellcomeapp.data.repository.EventRepository
import com.mistreckless.support.wellcomeapp.data.repository.UserRepository
import com.mistreckless.support.wellcomeapp.domain.interactor.*
import dagger.Module
import dagger.Provides
import wellcome.common.interactor.ShareInteractor
import wellcome.common.interactor.ShareInteractorImpl
import wellcome.common.location.LocationService
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
        userRepository: wellcome.common.repository.UserRepository,
        locationService: LocationService,
        eventRepository: wellcome.common.repository.EventRepository
    ): ShareInteractor = ShareInteractorImpl(locationService, userRepository, eventRepository)

    @Singleton
    @Provides
    fun provideWallInteractor(eventRepository: EventRepository): EventInteractor = EventInteractorImpl(eventRepository)

}
