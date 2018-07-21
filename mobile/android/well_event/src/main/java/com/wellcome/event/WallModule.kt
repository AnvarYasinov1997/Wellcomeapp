package com.wellcome.event

import android.arch.lifecycle.ViewModelProviders
import com.wellcome.ui.core.PerFragment
import dagger.Module
import dagger.Provides
import wellcome.common.core.repository.EventRepository
import wellcome.common.core.repository.UserRepository
import wellcome.common.core.service.StoryService
import wellcome.common.event.EventInteractor
import wellcome.common.event.EventInteractorImpl

@Module
class WallModule {

    @PerFragment
    @Provides
    fun provideViewModel(wall: Wall): WallViewModel = ViewModelProviders.of(wall)[WallViewModel::class.java]

    @PerFragment
    @Provides
    fun provideEventInteractor(eventRepository: EventRepository,
                               userRepository: UserRepository,
                               storyService: StoryService): EventInteractor =
        EventInteractorImpl(eventRepository, userRepository, storyService)
}