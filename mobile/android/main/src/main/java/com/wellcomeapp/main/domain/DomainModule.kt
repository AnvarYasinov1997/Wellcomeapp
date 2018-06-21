package com.wellcomeapp.main.domain

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
