package com.mistreckless.support.wellcomeapp.domain

import com.mistreckless.support.wellcomeapp.data.repository.UserRepository
import com.mistreckless.support.wellcomeapp.domain.interactor.DataInteractor
import com.mistreckless.support.wellcomeapp.domain.interactor.DataInteractorImpl
import com.mistreckless.support.wellcomeapp.domain.interactor.ProfileInteractor
import com.mistreckless.support.wellcomeapp.domain.interactor.ProfileInteractorImpl
import com.wellcome.core.service.StoryService
import dagger.Module
import dagger.Provides
import wellcome.common.interactor.*
import wellcome.common.location.LocationService
import wellcome.common.repository.EventRepository
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
    fun provideEventInteractor(eventRepository: EventRepository, storyService: StoryService): EventInteractor = EventInteractorImpl(eventRepository, storyService)

    @Singleton
    @Provides
    fun provideUserInteractor(userRepository: wellcome.common.repository.UserRepository): UserInteractor = UserInteractorImpl(userRepository)
}
